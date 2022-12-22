pragma solidity ^0.8.7;
// SPDX-License-Identifier: MIT

import "@openzeppelin/contracts/token/ERC20/IERC20.sol";
import "@openzeppelin/contracts/utils/math/Math.sol";

//Workflow
// deploy weth9
// deploy sampletoken
// deploy sampletokenethpool
// deposit weth
// approve weth for spending by sampletokenethpool
//

contract SimplePool {

    struct Pool {
        IERC20 token1;
        IERC20 token2;
        uint initialToken1Amount;
        uint token1Amount;
        uint token2Amount;
        uint token2VirtualAmount;
    }

    mapping(address => uint) public _poolIdByOwner;
    mapping(uint => address) public _poolOwnerById;

    mapping(uint => Pool) _pools;
    uint _poolsCount = 0;
    uint _maxTxPercent = 15;
    // Pool[] public _pools = new Pool[](0);

    constructor() {

    }

    function createPool(IERC20 token1, IERC20 token2, uint token1Amount, uint matchingPriceInToken2) payable external {
        // matchingPriceInToken2 is the requested initial amount for token2 that match token1
        uint poolId = _poolsCount;
        _poolsCount += 1;
        _poolIdByOwner[msg.sender] = poolId;
        _poolOwnerById[poolId] = msg.sender;
        token1.transferFrom(msg.sender, address(this), token1Amount);
        _pools[poolId].token1 = token1;
        _pools[poolId].token2 = token2;
        _pools[poolId].token1Amount = token1Amount;
        _pools[poolId].initialToken1Amount = token1Amount;
        _pools[poolId].token2Amount = 0;
        _pools[poolId].token2VirtualAmount = matchingPriceInToken2;
    }

    function exchangeToken(
        IERC20 tokenToBuy, 
        uint poolId, 
        uint tokenToSellAmount, 
        uint minReceiveTokenToBuyAmount
    ) payable external returns (uint) { // returns the amount of token bought.
        // tokenToSell must be the same as one of the tokens in the _pools[poolId]
        // tokenToBuy must be the same as one of the tokens in the pool
        Pool storage pool = _pools[poolId];
        require(tokenToBuy == pool.token1 || tokenToBuy == pool.token2, "trying to buy from wrong pool");
        // TODO extract the following in function and call depending which is token1 and token2
        if (tokenToBuy == pool.token1) {
            uint amountOut = Math.mulDiv(pool.token1Amount, tokenToSellAmount, pool.token2Amount + pool.token2VirtualAmount);
            amountOut = Math.min(amountOut, Math.mulDiv(pool.token1Amount, _maxTxPercent, 100));
            require(pool.token2.allowance(msg.sender, address(this)) >= tokenToSellAmount, "trying to sell more than you have");
            require(minReceiveTokenToBuyAmount <= amountOut,"minReceive is less than calcualted amount");
            // complete the transaction now
            require(pool.token2.transferFrom(msg.sender, address(this), tokenToSellAmount), "cannot transfer tokenToSellAmount");
            pool.token2Amount += tokenToSellAmount;
            require(pool.token1.transfer(msg.sender, amountOut), "cannot transfer from amountOut from pool");
            pool.token1Amount -= amountOut;
            return amountOut;
        } else if (tokenToBuy == pool.token2) {
            require(pool.token2Amount > 0, "zero amount of token for buy in pool");
            require(pool.initialToken1Amount > pool.token1Amount, "must have more than initial token1 amount");
            // TODO CALCULATE THIS ON PAPER
            uint amountOut = Math.mulDiv(tokenToSellAmount, pool.token2Amount, pool.initialToken1Amount - pool.token1Amount);
            amountOut = Math.min(amountOut, Math.mulDiv(pool.token2Amount, _maxTxPercent, 100));
            require(pool.token1.allowance(msg.sender, address(this)) >= tokenToSellAmount, "trying to sell more than allowance");
            require(minReceiveTokenToBuyAmount <= amountOut,"minReceive is less than calcualted amount");
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

    // TODO Add sell for token2 and vice versa (buy for token2 and sell for token1)

    function getPools() external view returns (Pool[] memory) {
        Pool[] memory pools = new Pool[](_poolsCount);
        for (uint i = 0; i < _poolsCount; ++i) {
            Pool storage pool = _pools[i];
            pools[i] = pool;
        }
        return pools;
    }
}
/*
First ECR20 have to approve token for spending by the pool contract and then the pool contract to deposit the token
*/
