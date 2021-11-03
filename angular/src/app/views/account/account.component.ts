import { Location } from "@angular/common";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { LoadingService } from "src/app/services/loading/loading.service";
import Web3 from "web3";
import { AuthGuardService } from "../../services/auth/auth-guard.service";

@Component({
  selector: 'account',
  templateUrl: './account.component.html',
  styleUrls: [ './account.component.css' ]
})
export class AccountComponent implements OnInit {

  withdrawTax = 0.005;
  address: string = '';
  balance = '';
  depositAddress = '';
  maxDepositAmount = 0;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private http: HttpClient,
    private auth: AuthGuardService,
    private loading: LoadingService,
  ) {}

  async ngOnInit() {
    this.address = this.auth.wallet;
    this.depositAddress = this.auth.depositAddress;
    this.balance = this.auth.balance;
    this.maxDepositAmount = this.auth.web3AccountBalance - this.withdrawTax;
  }

  async getBalance() {
    // Set loading indicator to get balance button...
    try {
      const headers: any = {
        WALLET: this.address,
        TOKEN: this.auth.token,
      }
      const resp = await this.http.get<any>("/services/balance",
        { headers: new HttpHeaders(headers) }
      ).toPromise();

      this.balance = Number(resp.data).toFixed(18);
      // Set timeout to reset the loading indicator after 1 minute...
    } catch (e: any) {
      console.error("Couldn't get balance");
      this.balance = "Couldn't extract the balance because of an error.";
    }
  }

  amountToWithdraw = 0;
  withdrawTransactionHash = "";

  async withdraw() {

    if (Number.isNaN(this.balance)) {
      console.error("Balance is not valid");
      return;
    }

    let amount = this.amountToWithdraw;
    if (Number.isNaN(amount)) {
      console.error("Invalid amount to withdraw");
      return;
    }
    if (amount <= this.withdrawTax) {
      console.error("Trying to withdraw less than the tax.");
      return;
    }

    if (Number(this.balance) - amount < 0) {
      console.error("Trying to withdraw more than I have.");
      return;
    }


    const headers: any = {
      WALLET: this.address,
      AMOUNT: this.amountToWithdraw,
      TOKEN: this.auth.token,
    };

    try {
      const res = await this.http.get<any>("/services/withdraw",
        { headers: new HttpHeaders(headers)} ).toPromise();
      this.withdrawTransactionHash = res.data;
    } catch (e: any) {
       console.log("Error withdrawing");
       this.withdrawTransactionHash = "Couldn't withdraw because of an error";
       return;
    }
  }

  amountToDeposit: number = 0;
  depositTxHash: string = '';

  async deposit() {
    this.loading.isLoading.next(true);
    try {
      if (!this.auth.web3) {
        console.error("Web3 is undefined.");
        // TODO IMPLEMENT ERROR NOTIFICATION SERVICE!!!
        return;
      }
      let web3: Web3 = this.auth.web3;
      let amountToDeposit = web3.utils.toWei(this.amountToDeposit.toString());
      let count = await web3.eth.getTransactionCount(this.auth.wallet);


      let txConfig: any = {
        "from": this.address,
        "gasPrice": this.auth.web3?.utils.toHex(5000000000),
        "gas": this.auth.web3?.utils.toHex(210000),
        "to": this.auth.depositAddress,
        "value": this.auth.web3?.utils.toHex(amountToDeposit),
        "nonce": count,
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
      }
      txConfig.common['baseChain'] = 'mainnet';
      txConfig.common['hardfork'] = 'istanbul';

      let receipt = await web3.eth.sendTransaction(txConfig);

      console.log(receipt);
      this.depositTxHash = receipt.transactionHash;

      await this.getBalance();
      
    } catch (e: any) {
      console.error(e);
    }
  }
}
