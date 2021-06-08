var fs = require('fs')

// CREATE ACCOUNT
var Accounts = require('web3-eth-accounts');
// Binance smart chain address
var accounts = new Accounts('ws://bsc-dataseed.binance.org/');

var accountsArray = [];
for (var i = 0; i < 50; ++i) {
    accountsArray.push(JSON.stringify(accounts.create()));
}
var accountsText = accountsArray.join('\n');
fs.writeFileSync('accounts.txt', accountsText);


// SEND TRANSACTION


// set token source, destination and amount

// set your private key here, we'll sign the transaction below

// Here you may get the abicode from a string or a file, here is a string case
// var abiArray = JSON.parse('[{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"bytes32"}],"payable":false,"type":"function"},{"constant":false,"inputs":[],"name":"stop","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"guy","type":"address"},{"name":"wad","type":"uint256"}],"name":"approve","outputs":[{"name":"","type":"bool"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"owner_","type":"address"}],"name":"setOwner","outputs":[],"payable":false,"type":"function"},{"constant":true,"inputs":[],"name":"totalSupply","outputs":[{"name":"","type":"uint256"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"src","type":"address"},{"name":"dst","type":"address"},{"name":"wad","type":"uint256"}],"name":"transferFrom","outputs":[{"name":"","type":"bool"}],"payable":false,"type":"function"},{"constant":true,"inputs":[],"name":"decimals","outputs":[{"name":"","type":"uint256"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"dst","type":"address"},{"name":"wad","type":"uint128"}],"name":"push","outputs":[{"name":"","type":"bool"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"name_","type":"bytes32"}],"name":"setName","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"wad","type":"uint128"}],"name":"mint","outputs":[],"payable":false,"type":"function"},{"constant":true,"inputs":[{"name":"src","type":"address"}],"name":"balanceOf","outputs":[{"name":"","type":"uint256"}],"payable":false,"type":"function"},{"constant":true,"inputs":[],"name":"stopped","outputs":[{"name":"","type":"bool"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"authority_","type":"address"}],"name":"setAuthority","outputs":[],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"src","type":"address"},{"name":"wad","type":"uint128"}],"name":"pull","outputs":[{"name":"","type":"bool"}],"payable":false,"type":"function"},{"constant":true,"inputs":[],"name":"owner","outputs":[{"name":"","type":"address"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"wad","type":"uint128"}],"name":"burn","outputs":[],"payable":false,"type":"function"},{"constant":true,"inputs":[],"name":"symbol","outputs":[{"name":"","type":"bytes32"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"dst","type":"address"},{"name":"wad","type":"uint256"}],"name":"transfer","outputs":[{"name":"","type":"bool"}],"payable":false,"type":"function"},{"constant":false,"inputs":[],"name":"start","outputs":[],"payable":false,"type":"function"},{"constant":true,"inputs":[],"name":"authority","outputs":[{"name":"","type":"address"}],"payable":false,"type":"function"},{"constant":true,"inputs":[{"name":"src","type":"address"},{"name":"guy","type":"address"}],"name":"allowance","outputs":[{"name":"","type":"uint256"}],"payable":false,"type":"function"},{"inputs":[{"name":"symbol_","type":"bytes32"}],"payable":false,"type":"constructor"},{"anonymous":true,"inputs":[{"indexed":true,"name":"sig","type":"bytes4"},{"indexed":true,"name":"guy","type":"address"},{"indexed":true,"name":"foo","type":"bytes32"},{"indexed":true,"name":"bar","type":"bytes32"},{"indexed":false,"name":"wad","type":"uint256"},{"indexed":false,"name":"fax","type":"bytes"}],"name":"LogNote","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"authority","type":"address"}],"name":"LogSetAuthority","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"owner","type":"address"}],"name":"LogSetOwner","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"from","type":"address"},{"indexed":true,"name":"to","type":"address"},{"indexed":false,"name":"value","type":"uint256"}],"name":"Transfer","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"owner","type":"address"},{"indexed":true,"name":"spender","type":"address"},{"indexed":false,"name":"value","type":"uint256"}],"name":"Approval","type":"event"}]', 'utf-8')
var targetAccounts = JSON.parse(fs.readFileSync('accounts.txt', 'utf-8'));

var sleep = require('system-sleep');
sleep(5*1000); // sleep for 5 seconds
var myAddress = fs.readFileSync('myaddress.txt', 'utf-8');

async function sendOnlyone(fromAddress, toAddress) {

    var Tx = require('ethereumjs-tx').Transaction;
    var Web3 = require('web3')
    var web3 = new Web3(new Web3.providers.HttpProvider('https://bsc-dataseed.binance.org/'))

    var amount = web3.utils.toHex(10)
    var privateKey = Buffer.from(fs.readFileSync('myAddressPrivKey.txt', 'utf-8'), 'hex');
    var abiArray = JSON.parse(JSON.parse(fs.readFileSync('onlyone-abi.json','utf-8')));
    var contractAddress = fs.readFileSync('token-contract-address.txt', 'utf-8')
    var contract = new web3.eth.Contract(abiArray, contractAddress, {from: fromAddress})
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



// check the balance
contract.methods.balanceOf(myAddress).call()
    .then(function(balance){
        console.log(balance)
    })
