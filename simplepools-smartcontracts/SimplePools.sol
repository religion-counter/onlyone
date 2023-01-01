pragma solidity ^0.8.7;
// SPDX-License-Identifier: MIT

import "@openzeppelin/contracts/token/ERC20/IERC20.sol";
import "@openzeppelin/contracts/utils/math/Math.sol";

// Workflow
// deploy usd token
// deploy sampletoken
// deploy simplepool
// deposit sampletoken
// approve sampletoken for spending by simplepool
// create pool with sampletoken and matching usd token

contract SimplePools {

    struct Pool {
        IERC20 token1;
        IERC20 token2;
        uint token1Amount;
        uint token2Amount;
        uint token2VirtualAmount;
        // For example when we want to use a pool for limit sell then maxtxpercent can be 100%
        // so someone can buy all the bitcoin/eth for sell with one transaction.
        // For new tokens that are only traded in one pool it can be 1% or 10% or 50%.
        uint maxPercentPerTransaction;
        uint constantProduct; // (T1*(T2+VT2))=ConstantProduct
        bool constantPrice;
        uint initialToken1Amount;
    }

    address[] public _poolOwnerById;
    mapping(uint => bool) public _lockedPools;
    mapping(uint => bool) public _emptyPools;

    Pool[] public _pools;

    // when we sync the state of all pools (from indexed DB node) we get the current number of all transactions
    // then we get only the latest transactions (that are not indexed) and sync the pools only from them
    // array of the pool ids of all transactions
    uint[] _allTransactionsPoolIds;

    constructor() {

    }

    function createPool(IERC20 token1, IERC20 token2,
            uint token1Amount, uint matchingPriceInToken2,
            uint maxPercentPerTransaction, bool constantPrice) external {
        // matchingPriceInToken2 is the requested initial amount for token2 that match token1
        uint poolId = _pools.length;
        _allTransactionsPoolIds.push(poolId);
        _poolOwnerById.push(msg.sender);
        token1.transferFrom(msg.sender, address(this), token1Amount);
        _pools.push().token1 = token1;
        _pools[poolId].token2 = token2;
        _pools[poolId].token1Amount = token1Amount;
        _pools[poolId].token2Amount = 0;
        _pools[poolId].token2VirtualAmount = matchingPriceInToken2;
        _pools[poolId].constantProduct = token1Amount * matchingPriceInToken2;
        _pools[poolId].maxPercentPerTransaction = maxPercentPerTransaction;
        _pools[poolId].constantPrice = constantPrice;
        _pools[poolId].initialToken1Amount = token1Amount;
    }

    function exchangeToken(
        IERC20 tokenToBuy, 
        uint poolId, 
        uint tokenToSellAmount, 
        uint minReceiveTokenToBuyAmount
    ) external returns (uint) { 
        require(!_emptyPools[poolId], "Pool is empty");
        // returns the amount of token bought.
        // tokenToSell must be the same as one of the tokens in the _pools[poolId]
        // tokenToBuy must be the same as one of the tokens in the pool
        Pool storage pool = _pools[poolId];
        require(tokenToBuy == pool.token1 || tokenToBuy == pool.token2, "trying to buy from wrong pool");
        _allTransactionsPoolIds.push(poolId);
        if (tokenToBuy == pool.token1) {
            uint amountOut;
            if (pool.constantPrice) {
                amountOut = Math.mulDiv(tokenToSellAmount, pool.initialToken1Amount, pool.token2VirtualAmount);
            } else {
                amountOut = pool.token1Amount -
                    Math.ceilDiv(pool.constantProduct,
                             pool.token2VirtualAmount + pool.token2Amount + tokenToSellAmount);
            }
            amountOut = Math.min(amountOut, Math.mulDiv(pool.token1Amount, pool.maxPercentPerTransaction, 100));
            require(pool.token2.allowance(msg.sender, address(this)) >= tokenToSellAmount, "trying to sell more than allowance");
            require(minReceiveTokenToBuyAmount <= amountOut,"minReceive is less than calcualted amount");
            // complete the transaction now
            require(pool.token2.transferFrom(msg.sender, address(this), tokenToSellAmount), "cannot transfer tokenToSellAmount");
            pool.token2Amount += tokenToSellAmount;
            require(pool.token1.transfer(msg.sender, amountOut), "cannot transfer from amountOut from pool");
            pool.token1Amount -= amountOut;
            return amountOut;
        } else if (tokenToBuy == pool.token2) {
            require(pool.token2Amount > 0, "zero amount of token for buy in pool");
            uint amountOut;
            if (pool.constantPrice) {
                amountOut = Math.mulDiv(tokenToSellAmount, pool.token2VirtualAmount, pool.initialToken1Amount);
            } else {
                amountOut = pool.token2VirtualAmount + pool.token2Amount 
                        - Math.ceilDiv(pool.constantProduct,
                               pool.token1Amount + tokenToSellAmount);
            }
            amountOut = Math.min(amountOut, Math.mulDiv(pool.token2Amount, pool.maxPercentPerTransaction, 100));
            require(pool.token1.allowance(msg.sender, address(this)) >= tokenToSellAmount, "trying to sell more than allowance");
            require(minReceiveTokenToBuyAmount <= amountOut,"minReceive is more than calcualted amount");
            // complete the transaction now
            require(pool.token1.transferFrom(msg.sender, address(this), tokenToSellAmount), "cannot transfer tokenToSellAmount");
            pool.token1Amount += tokenToSellAmount;
            require(pool.token2.transfer(msg.sender, amountOut), "cannot transfer from amountOut from pool");
            pool.token2Amount -= amountOut;
            return amountOut;
        }
        require(false, "Wrong token address or poolId");
        return 0;
    }

    function getAllTokensFromPool(uint poolId) external {
        require(_pools.length > poolId, "invalid pool id");
        require(!_lockedPools[poolId], "pool is locked");
        require(!_emptyPools[poolId], "pool is empty");
        require(_poolOwnerById[poolId] == msg.sender, "only the pool owner can empty pool");
        _allTransactionsPoolIds.push(poolId);
        Pool storage pool = _pools[poolId];
        pool.token1.transferFrom(address(this), msg.sender, pool.token1Amount);
        pool.token1Amount = 0;
        pool.token2.transferFrom(address(this), msg.sender, pool.token2Amount);
        pool.token2Amount = 0;
        pool.token2VirtualAmount = 0;
        _emptyPools[poolId] = true;
    }

    function lockPool(uint poolId) external returns (bool) {
        require(!_lockedPools[poolId], "pool is already locked");
        require(_pools.length > poolId, "invalid pool id");
        require(_poolOwnerById[poolId] == msg.sender, "only the pool owner can lock pool");
        _allTransactionsPoolIds.push(poolId);
        _lockedPools[poolId] = true;
        return true;
    }

    // if owner gets compromised and is fast enough or if you want to make 0 address the owner.
    function changeOwner(uint poolId, address newPoolOwner) external returns (bool) {
        require(poolId < _pools.length, "invalid poolId");
        require(!_lockedPools[poolId], "pool is locked");
        require(_poolOwnerById[poolId] == msg.sender, "only the pool owner can change ownership");
        _poolOwnerById[poolId] = newPoolOwner;
        _allTransactionsPoolIds.push(poolId);
        return true;
    }

    function changePoolMaxPercentPerTransaction(uint poolId, uint newMaxPercentPerTransaction) external returns (bool) {
        require(poolId < _pools.length, "invalid poolId");
        require(!_lockedPools[poolId], "pool is locked");
        require(_poolOwnerById[poolId] == msg.sender, "only the pool owner can change newMaxPercentPerTransaction");
        require(newMaxPercentPerTransaction <= 100 && newMaxPercentPerTransaction > 0, "invalid max percent per transaction");
        _pools[poolId].maxPercentPerTransaction = newMaxPercentPerTransaction;
        _allTransactionsPoolIds.push(poolId);
        return true;
    }

    function changeContantProduct(uint poolId, uint newConstnatProduct) external returns (bool) {
        require(poolId < _pools.length, "invalid poolId");
        require(!_lockedPools[poolId], "pool is locked");
        require(_poolOwnerById[poolId] == msg.sender, "only the pool owner can change the constant product");
        require(newConstnatProduct > 0, "invalid constant product (only positive numbers)");
        _pools[poolId].constantProduct = newConstnatProduct;
        _allTransactionsPoolIds.push(poolId);
        return true;
    }

    function isPoolLocked(uint poolId) external view returns (bool) {
        return _lockedPools[poolId];
    }

    function getPoolsCount() external view returns (uint) {
        return _pools.length;
    }

    // Get pools from start index to end index [startPoolIndex, ..., endPoolIndex)
    // start included, end not included
    function getPools(uint startPoolIndex, uint endPoolIndex) external view returns (Pool[] memory) {
       require(endPoolIndex > startPoolIndex && endPoolIndex <= _pools.length, "invalid indexes");
       Pool[] memory pools = new Pool[](endPoolIndex - startPoolIndex);
       for (uint i = startPoolIndex; i < endPoolIndex; ++i) {
            pools[i - startPoolIndex] = _pools[i];
        }
        return pools;
    }
    
    // till end
    function getPoolsFrom(uint startPoolIndex) external view returns (Pool[] memory) {
       require(startPoolIndex < _pools.length, "invalid index");
       Pool[] memory pools = new Pool[](_pools.length - startPoolIndex);
       for (uint i = startPoolIndex; i < _pools.length; ++i) {
            pools[i - startPoolIndex] = _pools[i];
        }
        return pools;
    }

    // Get pools specified in indexes array
    function getPools(uint[] memory indexes) external view returns (Pool[] memory) {
        Pool[] memory pools = new Pool[](indexes.length);
        for (uint i = 0; i < indexes.length; ++i) {
            Pool storage pool = _pools[indexes[i]];
            pools[i] = pool;
        }
        return pools;
    }

    function getPool(uint poolId) external view returns (Pool memory) {
        return _pools[poolId];
    }

    function getTransactionsCount() external view returns (uint) {
        return _allTransactionsPoolIds.length;
    }

    // [startPoolIndex, ..., endPoolIndex)
    // start included, end not included
    function getPoolsForTransactionsWithIndexesBetween(
            uint startTransactionIndex, uint endTransactionIndex) external view returns (uint[] memory) {
        require(endTransactionIndex > startTransactionIndex && endTransactionIndex <= _allTransactionsPoolIds.length, "invalid indexes");
        uint[] memory poolIndexes = new uint[](endTransactionIndex - startTransactionIndex);
        for (uint i = startTransactionIndex; i < endTransactionIndex; ++i) {
            poolIndexes[i - startTransactionIndex] = _allTransactionsPoolIds[i];
        }
        return poolIndexes;
    }

    function getPoolsForTransactionsWithIndexesFrom(
            uint startTransactionIndex) external view returns (uint[] memory) {
        require(startTransactionIndex < _allTransactionsPoolIds.length, "invalid index");
        uint[] memory poolIndexes = new uint[](_allTransactionsPoolIds.length - startTransactionIndex);
        for (uint i = startTransactionIndex; i < _allTransactionsPoolIds.length; ++i) {
            poolIndexes[i - startTransactionIndex] = _allTransactionsPoolIds[i];
        }
        return poolIndexes;
    }
}
/*
First ECR20 have to approve token for spending by the pool contract and then the pool contract can deposit the token
*/
