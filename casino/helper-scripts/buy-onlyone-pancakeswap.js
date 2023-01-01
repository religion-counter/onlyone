// Helper script that buys ONLYONE token from a specified address specified on text file SPECIFY_ACCOUNTS_YOU_WANT_TO_BUY_FOR_HERE.json
// The amount is specified with 'originalAmountToBuyWith' variable in the source
// The JSON file should have an array with objects with 'address' field and 'privateKey' field.
// Buys ONLYONE for ${bnbAmount} BNB from pancakeswap for address ${targetAccounts[targetIndex].address}
// targetIndex is passed as an argument: process.argv.splice(2)[0]

var fs = require('fs')
var Tx = require('ethereumjs-tx').Transaction;
var Web3 = require('web3')
var Common = require('ethereumjs-common').default;

var web3 = new Web3(new Web3.providers.HttpProvider('https://bsc-dataseed.binance.org/'))
var BSC_FORK = Common.forCustomChain(
    'mainnet',
    {
        name: 'Binance Smart Chain Mainnet',
        networkId: 56,
        chainId: 56,
        url: 'https://bsc-dataseed.binance.org/'
    },
    'istanbul',
);

// SPECIFY_THE_AMOUNT_OF_BNB_YOU_WANT_TO_BUY_FOR_HERE
var originalAmountToBuyWith = '0.007' + Math.random().toString().slice(2,7);
var bnbAmount = web3.utils.toWei(originalAmountToBuyWith, 'ether');

var targetAccounts = JSON.parse(fs.readFileSync('SPECIFY_ACCOUNTS_YOU_WANT_TO_BUY_FOR_HERE.json', 'utf-8'));

var targetIndex = Number(process.argv.splice(2)[0]);
var targetAccount = targetAccounts[targetIndex];

console.log(`Buying ONLYONE for ${originalAmountToBuyWith} BNB from pancakeswap for address ${targetAccount.address}`);

var res = buyOnlyone(targetAccounts[targetIndex], bnbAmount);
console.log(res);

async function buyOnlyone(targetAccount, amount) {

    var amountToBuyWith = web3.utils.toHex(amount);
    var privateKey = Buffer.from(targetAccount.privateKey.slice(2), 'hex')  ;
    var abiArray = JSON.parse(JSON.parse(fs.readFileSync('onlyone-abi.json','utf-8')));
    var tokenAddress = '0xb899db682e6d6164d885ff67c1e676141deaaa40'; // ONLYONE contract address
    var WBNBAddress = '0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c'; // WBNB token address

    // var onlyOneWbnbCakePairAddress = '0xd22fa770dad9520924217b51bf7433c4a26067c2';
    // var pairAbi = JSON.parse(fs.readFileSync('cake-pair-onlyone-bnb-abi.json', 'utf-8'));
    // var pairContract = new web3.eth.Contract(pairAbi, onlyOneWbnbCakePairAddress/*, {from: targetAccount.address}*/);
    var amountOutMin = '100' + Math.random().toString().slice(2,6);
    var pancakeSwapRouterAddress = '0x10ed43c718714eb63d5aa57b78b54704e256024e';

    var routerAbi = JSON.parse(fs.readFileSync('pancake-router-abi.json', 'utf-8'));
    var contract = new web3.eth.Contract(routerAbi, pancakeSwapRouterAddress, {from: targetAccount.address});
    var data = contract.methods.swapExactETHForTokens(
        web3.utils.toHex(amountOutMin),
        [WBNBAddress,
         tokenAddress],
        targetAccount.address,
        web3.utils.toHex(Math.round(Date.now()/1000)+60*20),
    );

    var count = await web3.eth.getTransactionCount(targetAccount.address);
    var rawTransaction = {
        "from":targetAccount.address,
        "gasPrice":web3.utils.toHex(5000000000),
        "gasLimit":web3.utils.toHex(290000),
        "to":pancakeSwapRouterAddress,
        "value":web3.utils.toHex(amountToBuyWith),
        "data":data.encodeABI(),
        "nonce":web3.utils.toHex(count)
    };

    var transaction = new Tx(rawTransaction, { 'common': BSC_FORK });
    transaction.sign(privateKey);

    var result = await web3.eth.sendSignedTransaction('0x' + transaction.serialize().toString('hex'));
    console.log(result)
    return result;
}

