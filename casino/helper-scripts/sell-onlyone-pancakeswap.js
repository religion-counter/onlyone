// Sells ONLYONE for BNB from pancakeswap for address ${targetAccounts[targetIndex].address}

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


var targetAccounts = JSON.parse(fs.readFileSync('FILE_WITH_ACCOUNT_ADDRESS_AND_PRIVATE_KEY.json', 'utf-8'));

sellMulty();

async function sellMulty() {
    for (var i = 1; i < 2; ++i) {
        var targetAccount = targetAccounts[i];
        var onyloneAmount = 0.00000001; // Amount you want to sell
        console.log(`${i}: Selling ${onyloneAmount} ONLYONE for BNB to pancakeswap for address ${targetAccount.address}`);
        var res = sellOnlyone(targetAccount, onyloneAmount*1e18)
            .catch(e => {
                console.error("Error in sell:", e);
                process.exit(1);
            });
        console.log(res);
        await sleep(5000 + Math.random().toFixed(4)*10000);
    }
}

async function sellOnlyone(targetAccount, amount) {

    var amountToSell = web3.utils.toHex(amount);
    var privateKey = Buffer.from(targetAccount.privateKey.slice(2), 'hex')  ;
    var abiArray = JSON.parse(JSON.parse(fs.readFileSync('onlyone-abi.json','utf-8')));

    var tokenAddress = '0xb899db682e6d6164d885ff67c1e676141deaaa40'; // ONLYONE contract address
    var WBNBAddress = '0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c'; // WBNB token address
    var pancakeSwapRouterAddress = '0x10ed43c718714eb63d5aa57b78b54704e256024e';

    // Approve ONLYONE spend
    var onlyoneContract = new web3.eth.Contract(abiArray, tokenAddress, {from: targetAccount.address});
    var approveOnlyoneSpendData = onlyoneContract.methods.approve(pancakeSwapRouterAddress, web3.utils.toWei('1', 'ether'));
    var count = await web3.eth.getTransactionCount(targetAccount.address);
    var rawTransactionApprove = {
        "from":targetAccount.address,
        "gasPrice":web3.utils.toHex(5000000000),
        "gasLimit":web3.utils.toHex(210000),
        "to":tokenAddress,
        "value":"0x0",
        "data":approveOnlyoneSpendData.encodeABI(),
        "nonce":web3.utils.toHex(count)
    };
    var transactionApprove = new Tx(rawTransactionApprove, {'common':BSC_FORK});
    transactionApprove.sign(privateKey)

    var resultApprove = await web3.eth.sendSignedTransaction('0x' + transactionApprove.serialize().toString('hex'));
    console.log("Approved" + resultApprove);

    // var onlyOneWbnbCakePairAddress = '0xd22fa770dad9520924217b51bf7433c4a26067c2';
    // var pairAbi = JSON.parse(fs.readFileSync('cake-pair-onlyone-bnb-abi.json', 'utf-8'));
    // var pairContract = new web3.eth.Contract(pairAbi, onlyOneWbnbCakePairAddress/*, {from: targetAccount.address}*/);
    var amountOutMin = web3.utils.toHex(amount*540); // 540BNB is the price of one onlyone

    var routerAbi = JSON.parse(fs.readFileSync('pancake-router-abi.json', 'utf-8'));
    var contract = new web3.eth.Contract(routerAbi, pancakeSwapRouterAddress, {from: targetAccount.address});
    var data = contract.methods.swapExactTokensForETHSupportingFeeOnTransferTokens(
        amountToSell,
        amountOutMin,
        [tokenAddress,
        //  '0xe9e7cea3dedca5984780bafc599bd69add087d56' /* BUSD address */, // Add this if you want to go through the onlyone-busd pair
         WBNBAddress],
        targetAccount.address,
        web3.utils.toHex(Math.round(Date.now()/1000)+60*20),
    );

    count = await web3.eth.getTransactionCount(targetAccount.address);
    var rawTransaction = {
        "from":targetAccount.address,
        "gasPrice":web3.utils.toHex(5000000000),
        "gasLimit":web3.utils.toHex(460000),
        "to":pancakeSwapRouterAddress,
        "value":web3.utils.toHex(0),
        "data":data.encodeABI(),
        "nonce":web3.utils.toHex(count)
    };

    var transaction = new Tx(rawTransaction, { 'common': BSC_FORK });
    transaction.sign(privateKey);

    var result = await web3.eth.sendSignedTransaction('0x' + transaction.serialize().toString('hex'));
    console.log(result)
    return result;
}

function sleep(ms) {
    return new Promise((resolve) => {
        setTimeout(resolve, ms);
    });
} 

