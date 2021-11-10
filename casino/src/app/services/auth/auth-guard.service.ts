import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import Web3 from 'web3';

@Injectable({ providedIn: 'root' })
export class AuthGuardService implements CanActivate {

  constructor(public router: Router) {}

  token: string | undefined;
  accountAddress: string | undefined;
  bnbBalance: number | undefined = undefined;
  onlyoneBalance: number | undefined = undefined;
  depositAddress: string | undefined;
  web3: Web3 | undefined = undefined;
  web3AccountBnbBalance: number | undefined;
  web3AccountOnlyoneBalance: number | undefined;

  onlyoneContract: any;

  onlyoneTokenAddress = '0xb899db682e6d6164d885ff67c1e676141deaaa40';

  canActivate(): boolean {
    if (this.token?.length) {
      return true;
    }
    this.router.navigate(['login']);
    return false;
  }

  async sendOnlyone(toAddress: string, amount: number): Promise<string | undefined> {
    let count = await this.web3?.eth.getTransactionCount(this.accountAddress as string);

    let rawAmount = this.web3?.utils.toHex(amount*1e18);
    let transactionData = this.onlyoneContract.methods.transfer(toAddress, rawAmount).encodeABI();
    let rawTransaction: any = {
      "from": this.accountAddress,
      "gasPrice": this.web3?.utils.toHex(5000000000),
      "gasLimit": this.web3?.utils.toHex(660000),
      "to": this.onlyoneTokenAddress,
      "value":"0x0",
      "data": transactionData,
      "nonce": this.web3?.utils.toHex(count as number),
      "chainId": 56,
      "chain": "mainnet",
      "hardfork": "instanbul",
      "common": {
        "customChain": {
            "name": 'Binance Smart Chain Mainnet',
            "networkId": 56,
            "chainId": 56,
          },
          // "baseChain": 'mainnet',
          // "hardfork": 'istanbul',
        },
    };
    rawTransaction.common['baseChain'] = 'mainnet';
    rawTransaction.common['hardfork'] = 'istanbul';

    let receipt = await this.web3?.eth.sendTransaction(rawTransaction);

    console.log(receipt);
    return receipt?.transactionHash;
  }

}
