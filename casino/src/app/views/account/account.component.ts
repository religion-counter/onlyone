import { Location } from "@angular/common";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { BalanceService } from "src/app/services/balance/balance.service";
import { ErrorService } from "src/app/services/error/error.service";
import { LoadingService } from "src/app/services/loading/loading.service";
import Web3 from "web3";
import { AuthGuardService } from "../../services/auth/auth-guard.service";

@Component({
  selector: 'account',
  templateUrl: './account.component.html',
  styleUrls: [ './account.component.css' ]
})
export class AccountComponent implements OnInit {

  get accountAddress(): string | undefined {
    return this.auth.accountAddress;
  };

  get maxDepositBnbAmount(): number | undefined {
    return this.auth.web3AccountBnbBalance; // TODO extract it from auth service and make it part of hello response
  }

  get bnbBalance(): number | undefined {
    return this.auth.bnbBalance;
  }

  amountBnbToDeposit: number | undefined;

  depositBnb() {

  }


  depositBnbTxHash: string | undefined;

  get onlyoneBalance(): string | undefined {
    return this.auth.onlyoneBalance?.toFixed(18);
  }

  get maxDepositOnlyoneAmount(): number | undefined {
    return this.auth.web3AccountOnlyoneBalance;
  }

  amountOnlyoneToDeposit: number | undefined;

  async depositOnlyone() {
    try {
     this.loading.isLoading.next(true);
     let txHash = await this.auth.sendOnlyone(this.auth.depositAddress as string, this.amountOnlyoneToDeposit as number);
     this.depositOnlyoneTxHash = txHash as string;
     this.refreshBalance();
    } catch (e: any) {
      this.errorService.showError(e.message);
    }
  }

  depositOnlyoneTxHash: string | undefined;

  get depositAddress(): string | undefined {
    return this.auth.depositAddress;
  }

  async refreshBalance() {
    this.balanceService.refreshBalance();
  }

  amountBnbToWithdraw: number | undefined;

  withdrawBnb() {

  }

  withdrawTaxBnb = 0.00021;
  
  withdrawBnbTransactionHash: string | undefined;

  amountOnlyoneToWithdraw: number | undefined;

  withdrawTaxOnlyone: string | undefined = '0.0000001';

  withdrawOnlyoneTransactionHash: string | undefined;

  withdrawOnlyone() {

  }

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private http: HttpClient,
    private auth: AuthGuardService,
    private loading: LoadingService,
    private errorService: ErrorService,
    private balanceService: BalanceService,
  ) {}

  async ngOnInit() {
  }


  async withdraw() {

    // if (Number.isNaN(this.balance)) {
    //   console.error("Balance is not valid");
    //   this.errorMessage = "Balance is not valid";
    //   return;
    // }

    // let amount = this.amountToWithdraw;
    // if (Number.isNaN(amount)) {
    //   console.error("Invalid amount to withdraw");
    //   this.errorMessage = "Invalid amount to withdraw";
    //   return;
    // }
    // if (amount <= this.withdrawTax) {
    //   console.error("Trying to withdraw less than the tax.");
    //   this.errorMessage = "Trying to withdraw less than the tax.";
    //   return;
    // }

    // if (Number(this.balance) - amount < 0) {
    //   console.error("Trying to withdraw more than you have.");
    //   this.errorMessage = "Trying to withdraw more than you have.";
    //   return;
    // }
    
    const headers: any = {
      WALLET: this.accountAddress,
      // AMOUNT: this.amountToWithdraw.toString(),
      TOKEN: this.auth.token,
    };

    try {
      const res = await this.http.get<any>("/services/withdraw",
        { headers: new HttpHeaders(headers)} ).toPromise();
      // this.withdrawTransactionHash = res.data;
      // TODO Update balance on response with the new balance ( after the withdraw )
    } catch (e: any) {
       console.log("Error withdrawing");
      //  this.withdrawTransactionHash = "Couldn't withdraw because of an error";
       return;
    }
  }

  amountToDeposit: number = 0;
  depositTxHash: string = '';

  async deposit() {
    // this.errorMessage = '';

    // this.loading.isLoading.next(true);
    // try {
    //   if (!this.auth.web3) {
    //     console.error("Web3 is undefined.");
    //     // TODO IMPLEMENT ERROR NOTIFICATION SERVICE!!!
    //     return;
    //   }
    //   let web3: Web3 = this.auth.web3;
    //   if (this.amountToDeposit === 0) {
    //     this.errorMessage = "You cannot deposit zero amount.";
    //     console.log(this.errorMessage);
    //     return;
    //   }
    //   let amountToDeposit = web3.utils.toWei(this.amountToDeposit.toString());
    //   let count = await web3.eth.getTransactionCount(this.auth.wallet);


    //   let txConfig: any = {
    //     "from": this.address,
    //     "gasPrice": this.auth.web3?.utils.toHex(5000000000),
    //     "gas": this.auth.web3?.utils.toHex(210000),
    //     "to": this.auth.depositAddress,
    //     "value": this.auth.web3?.utils.toHex(amountToDeposit),
    //     "nonce": count,
    //     "chainId": 56,
    //     "chain": "mainnet",
    //     "hardfork": "instanbul",
    //     "common": {
    //       "customChain": {
    //         "name": 'Binance Smart Chain Mainnet',
    //         "networkId": 56,
    //         "chainId": 56,
    //       },
    //       // "baseChain": 'mainnet',
    //       // "hardfork": 'istanbul',
    //     },
    //   }
    //   txConfig.common['baseChain'] = 'mainnet';
    //   txConfig.common['hardfork'] = 'istanbul';

    //   let receipt = await web3.eth.sendTransaction(txConfig);

    //   console.log(receipt);
    //   this.depositTxHash = receipt.transactionHash;

    //   await this.getBalance();
      
    // } catch (e: any) {
    //   this.errorMessage = 
    //       "Couldn't deposit fund to your account: " + e.message;
    //   console.error(e);
    // }
  }
}
