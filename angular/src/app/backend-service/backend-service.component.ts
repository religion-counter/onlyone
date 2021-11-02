import { Location } from "@angular/common";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { __core_private_testing_placeholder__ } from "@angular/core/testing";
import { ActivatedRoute } from "@angular/router";
import Web3 from "web3";
import { AuthGuardService } from "../auth/auth-guard.service";

@Component({
  selector: 'backend-service',
  templateUrl: './backend-service.component.html',
  styleUrls: [ './backend-service.component.css' ]
})
export class BackendServiceComponent implements OnInit {

  withdrawTax = 0.005;
  address: string = '';
  balance = '';
  depositAddress = '';

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private http: HttpClient,
    private auth: AuthGuardService,
  ) {}

  async ngOnInit() {
    this.address = this.auth.wallet;
    this.depositAddress = this.auth.depositAddress;
    this.balance = this.auth.balance;
  }

  showLoadingSpinner = false;

  async getBalance() {
    // Set loading indicator to get balance button...
    this.showLoadingSpinner = true;
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
      setTimeout(() => {
        this.showLoadingSpinner = false;
      }, 5000);
    } catch (e: any) {
      console.error("Couldn't get balance");
      this.balance = "Couldn't extract the balance because of an error.";
    }
  }

  amountToWithdraw = ''
  withdrawTransactionHash = "";

  async withdraw() {

    if (Number.isNaN(this.balance)) {
      console.error("Balance is not valid");
      return;
    }

    let amount = Number.parseFloat(this.amountToWithdraw);
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

    this.showLoadingSpinner = true;

    const headers: any = {
      WALLET: this.address,
      AMOUNT: this.amountToWithdraw,
      TOKEN: this.auth.token,
    };

    try {
      const res = await this.http.get<any>("/services/withdraw",
        { headers: new HttpHeaders(headers)} ).toPromise();
      this.withdrawTransactionHash = res.data;

      setTimeout(() => {
        this.showLoadingSpinner = false;
      }, 5000);
    } catch (e: any) {
       console.log("Error withdrawing");
       this.withdrawTransactionHash = "Couldn't withdraw because of an error";
       return;
    }
  }
}
