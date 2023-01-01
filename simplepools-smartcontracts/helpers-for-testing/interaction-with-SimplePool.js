const fs = require('fs');
const { Common, Chain, Hardfork } = require('@ethereumjs/common');
const SimplePoolContractAddress = '0x2E39e2b87Ee3885D5D46aA0133b43898f240e58B';
const Token1Address = '0xb00a1801A3e7c5824419A63f228851ca68E2f277';
const USDTAddress = '0x8A03529A1F5C6b3f60B534Ad07B7A38A70e8cE06';

const GasPrice = 2500000000;
const GasLimit = 260000;

const { Transaction } = require('@ethereumjs/tx');
const Web3 = require('web3');

const web3 = new Web3(new Web3.providers.HttpProvider('https://sepolia.infura.io/v3/253e734c2e1e4e64b1fa5fc62189f2bd'));
const SEPOLIA = new Common({chain: Chain.Sepolia});

const accPkJson = JSON.parse(fs.readFileSync('testwwt.pk.json'));

const simplePoolJson = JSON.parse(fs.readFileSync('artifacts/SimplePool.json'));
const SimplePoolContract =
    new web3.eth.Contract(
        simplePoolJson.abi,
        SimplePoolContractAddress,
        { from: accPkJson.address },
    );

async function getPoolData() {
    const poolData = await SimplePoolContract.methods.getPools().call();
    console.log(poolData);
}

async function swapTest() {
    const txData = SimplePoolContract.methods.exchangeToken(
        Token1Address,
        0,
        100,
        0
    );
    const count = await web3.eth.getTransactionCount(accPkJson.address);
    const rawTx = {
        from: accPkJson.address,
        gasPrice: web3.utils.toHex(GasPrice),
        gasLimit: web3.utils.toHex(GasLimit),
        to: SimplePoolContractAddress,
        value: "0x0",
        data: txData.encodeABI(),
        nonce: web3.utils.toHex(count),
    };
    let tx = new Transaction(
        rawTx,
        { common: SEPOLIA }
    );
    const privateKey = Buffer.from(accPkJson.pk.slice(2), 'hex');
    tx = tx.sign(privateKey);
    const result = await web3.eth.sendSignedTransaction
        ('0x' + tx.serialize().toString('hex'));
    console.log(result);
};

async function sleep(timeout) {
    return new Promise((resolve) => setTimeout(resolve, timeout));
}

async function createPoolTest() {
    const txData = SimplePoolContract.methods.createPool(
        Token1Address,
        USDTAddress,
        100,
        100
    );
    const count = await web3.eth.getTransactionCount(accPkJson.address);
    const rawTx = {
        from: accPkJson.address,
        gasPrice: web3.utils.toHex(GasPrice),
        gasLimit: web3.utils.toHex(GasLimit),
        to: SimplePoolContractAddress,
        value: "0x0",
        data: txData.encodeABI(),
        nonce: web3.utils.toHex(count),
    };
    let tx = new Transaction(
        rawTx,
        { common: SEPOLIA }
    );
    const privateKey = Buffer.from(accPkJson.pk.slice(2), 'hex');
    tx = tx.sign(privateKey);
    const result = await web3.eth.sendSignedTransaction
        ('0x' + tx.serialize().toString('hex'));
    console.log(result);
};

async function main() {
    for (let i = 1737; i < 6000; ++i) {
        console.log(`creating pool ${i}`);
        try {
            await createPoolTest();        
        } catch (err) {
            console.error(err);

        }
        const sleepSeconds = 5;
        console.log(`Sleeping for ${sleepSeconds} seconds`);
        await sleep(sleepSeconds * 1000);
        console.log(`Slept for ${sleepSeconds} seconds`);
    }
    await getPoolData();    
}

main();
// swapTest();
