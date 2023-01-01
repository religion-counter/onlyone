// Helper script that sends ONLYONE token to target addresses specified in targets.txt
// Target index - index in targets.txt file is specified as an argument - process.argv.splice(2)[0]

var fs = require('fs')

var targetAccounts = JSON.parse(fs.readFileSync('targets.txt', 'utf-8'));

var myAddress = JSON.parse(fs.readFileSync("my-address.json", 'utf-8'));
var targetIndex = Number(process.argv.splice(2)[0]);

console.log(`Sending ONLYONE to target ${targetIndex}.`);

async function sendOnlyone(fromAddress, toAddress) {

    var Tx = require('ethereumjs-tx').Transaction;
    var Web3 = require('web3');
    var web3 = new Web3(new Web3.providers.HttpProvider('https://bsc-dataseed.binance.org/'));

    var amount = web3.utils.toHex(10);
    var privateKey = Buffer.from(myAddress.privateKey, 'hex');
    var abiArray = JSON.parse(JSON.parse(fs.readFileSync('onlyone-abi.json','utf-8')));
    var contractAddress = '0xb899db682e6d6164d885ff67c1e676141deaaa40'; // ONLYONE address
    var contract = new web3.eth.Contract(abiArray, contractAddress, {from: fromAddress});
    var Common = require('ethereumjs-common').default;
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

    var count = await web3.eth.getTransactionCount(myAddress);

    var rawTransaction = {
        "from":myAddress,
        "gasPrice":web3.utils.toHex(5000000000),
        "gasLimit":web3.utils.toHex(210000),
        "to":contractAddress,"value":"0x0",
        "data":contract.methods.transfer(toAddress, amount).encodeABI(),
        "nonce":web3.utils.toHex(count)
    };

    var transaction = new Tx(rawTransaction, {'common':BSC_FORK});
    transaction.sign(privateKey)

    var result = await web3.eth.sendSignedTransaction('0x' + transaction.serialize().toString('hex'));
    return result;
}

sendOnlyone(myAddress, targetAccounts[targetIndex]);
