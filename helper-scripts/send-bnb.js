// Sends ${amountToSend} BNB from ${sourceAccount.address} to ${targetAccounts[targetIndex].address}
// targetIndex is passed as argument - process.argv.splice(2)[0];
// sourceAccount is specified in SOURCE_ACC.json
// targetAccounts are specified in TARGET_ACCS.json
// The sourceAccount is just an object with address and privateKey values.
// targetAccounts are JSON with Array with objects with address value.
// originalAmountToSend is the amount that the script sends

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

var originalAmountToSend = '0.01';
var amountToSend = web3.utils.toWei(originalAmountToSend, 'ether');
var sourceAccount = JSON.parse(fs.readFileSync('SOURCE_ACC.json', 'utf-8'));
var targetAccounts = JSON.parse(fs.readFileSync('TARGET_ACCS.json', 'utf-8'));

var targetIndex = Number(process.argv.splice(2)[0]);

console.log(`Sending ${originalAmountToSend} BNB from ${sourceAccount.address} to ${targetAccounts[targetIndex].address}`);

async function sendBNB(fromAddress, toAddress, pk, amountToSend) {

    var privateKey = Buffer.from(pk.slice(2), 'hex');
    var count = await web3.eth.getTransactionCount(fromAddress);

    var rawTransaction = {
        "from": fromAddress,
        "gasPrice": web3.utils.toHex(5000000000),
        "gasLimit": web3.utils.toHex(220000),
        "to": toAddress,
        "value": web3.utils.toHex(amountToSend),
        "nonce": web3.utils.toHex(count)
    };

    var transaction = new Tx(rawTransaction, { 'common': BSC_FORK });
    transaction.sign(privateKey);

    var result = await web3.eth.sendSignedTransaction('0x' + transaction.serialize().toString('hex'));
    console.log(result);
    return result;
}

console.log(
    sendBNB(sourceAccount.address, targetAccounts[targetIndex].address, sourceAccount.privateKey, amountToSend));
