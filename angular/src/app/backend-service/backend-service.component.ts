import { Location } from "@angular/common";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { __core_private_testing_placeholder__ } from "@angular/core/testing";
import { ActivatedRoute } from "@angular/router";
import Web3 from "web3";

@Component({
  selector: 'backend-service',
  templateUrl: './backend-service.component.html',
  styleUrls: [ './backend-service.component.css' ]
})
export class BackendServiceComponent implements OnInit {

    withdrawTax = 0.005;
    value: string = "";
    address: string = "";
    balance = "";
    message = "";

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private http: HttpClient,
  ) {}

  async ngOnInit() {

    let web3;
    if (typeof (window as any).ethereum !== 'undefined') {
      web3 = new Web3(Web3.givenProvider);
      console.log('Web3 wallet is installed!');
    } else {
        // add message and hide connect button.
        console.error("Please install an web3 wallet in order to use the site.");
        return;
    }
    const ethereum: any = (window as any).ethereum;

    try {
      await ethereum.request({ method: 'wallet_addEthereumChain', 
              params: [{ chainId: '0x38', 
                          chainName: 'Binance Smart Chain',
                          nativeCurrency: { name: 'BNB', symbol: 'BNB', decimals: 18 }, 
                          rpcUrls: ['https://bsc-dataseed.binance.org/'], 
                          blockExplorerUrls: ['https://bscscan.com/'] 
                      }] 
      });
      const accounts = await web3.eth.requestAccounts(); //ethereum.request({ method: 'eth_requestAccounts' });
      const account = web3.utils.toChecksumAddress(accounts[0]);
      const balance = await web3.eth.getBalance(account);
      this.address = account;
      // this.balance = web3.utils.fromWei(balance) + " BNB";

      let headers: any = {
        WALLET: account,
      }
      const signMessageObj = await this.http.get<any>(
        "/services/getSignMessage",
        { headers: new HttpHeaders(headers) }).toPromise()
      const signMessage = signMessageObj.data;
      const signature = await web3.eth.personal.sign(signMessage, account, "");
      // TODO Check the signature with the DB. If the signature match with the DB - log in the user.

      // here we send the signature and account to the backend and if the account exists
      // then show the casino balance.
      // if no - add account and signature to the DB,
      // associate newly created wallet to the account
      // when the user deposit - get the money from the created account
      // when they arrive - transfer them to the casino account and refresh the user's balance with the new money
      // Withdraw sends from the casino account to the user wallet. Withdraw tax - 2 times transaction tax (Cover the deposit)

      console.log(signature);
      headers['SIGNATURE'] = signature;

      const response = await this.http.get<any>("/services/hello",
          { headers: new HttpHeaders(headers) }).toPromise();

      // TODO Set Cookie in the response.
      console.log(response.data);
      this.value = "Successfull log in. Your deposit address is: " + response.data;
    } catch (e: any) {
        this.message += e.message;
        this.message += "Web3 not connected.";
        this.address = "Not connected";
        this.balance = "Not connected";
    }
  }

  showBalanceSpinner = false;

  async getBalance() {
    // Set loading indicator to get balance button...
    this.showBalanceSpinner = true;
    try {
      const headers: any = {
        WALLET: this.address,
      }
      const resp = await this.http.get<any>("/services/balance",
        { headers: new HttpHeaders(headers) }
      ).toPromise();

      this.balance = Number(resp.data).toFixed(18);
      // Set timeout to reset the loading indicator after 1 minute...
      setTimeout(() => {
        this.showBalanceSpinner = false;
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

    const headers: any = {
      WALLET: this.address,
      AMOUNT: this.amountToWithdraw,
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
}
