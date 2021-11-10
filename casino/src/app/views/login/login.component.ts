import { Location } from "@angular/common";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { __core_private_testing_placeholder__ } from "@angular/core/testing";
import { ActivatedRoute, Router } from "@angular/router";
import { ErrorService } from "src/app/services/error/error.service";
import { environment } from "src/environments/environment";
import Web3 from "web3";
import { AuthGuardService } from "../../services/auth/auth-guard.service";
import { LoadingService } from "../../services/loading/loading.service";
import * as abi from "../../services/abi/onlyone-abi.json";

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: [ './login.component.css' ]
})
export class LoginComponent implements OnInit {

    errorMessage = '';

    constructor(
        private auth: AuthGuardService,
        private route: ActivatedRoute,
        private location: Location,
        private http: HttpClient,
        private router: Router,
        private loading: LoadingService,
        private errorService: ErrorService,
    ){
    }

    async ngOnInit( ) {
    }

    async login() {
        this.errorMessage = '';
        this.loading.isLoading.next(true);
        if (typeof (window as any).ethereum !== 'undefined') {
            this.auth.web3 = new Web3(Web3.givenProvider);
        } else {
            // add message and hide connect button.
            console.error("Please install an web3 wallet in order to use the site.");
            this.errorMessage = "Please install an web3 wallet in order to use the site.";
            this.errorService.showError("Please install an web3 wallet in order to use the site.");
            this.loading.isLoading.next(false);
            return;
        }
        const ethereum: any = (window as any).ethereum;
        if (!environment.production) {
            (window as any).web3 = this.auth.web3;
        }

        try {
            await ethereum.request({ method: 'wallet_addEthereumChain', 
                    params: [{ chainId: '0x38', 
                                chainName: 'Binance Smart Chain',
                                nativeCurrency: { name: 'BNB', symbol: 'BNB', decimals: 18 }, 
                                rpcUrls: ['https://bsc-dataseed.binance.org/'], 
                                blockExplorerUrls: ['https://bscscan.com/'] 
                            }] 
            });
            const accounts = await this.auth.web3.eth.requestAccounts(); //ethereum.request({ method: 'eth_requestAccounts' });
            const account = this.auth.web3.utils.toChecksumAddress(accounts[0]);
            const balance = await this.auth.web3.eth.getBalance(account);
            this.auth.web3AccountBnbBalance = Number(balance)/1e18;

            // TODO Assign it to auth.
            const abiArray = (abi as any).default;
            this.auth.onlyoneContract = new this.auth.web3.eth.Contract(
                abiArray,
                 this.auth.onlyoneTokenAddress, 
                { from: account }
            );

            this.auth.accountAddress = account;
            // this.balance = web3.utils.fromWei(balance) + " BNB";

            let headers: any = {
                WALLET: account,
            }
            const signMessage = await this.http.get<string>(
                "/services/getSignMessage",
                { headers: new HttpHeaders(headers) }).toPromise()
            const signature = await this.auth.web3.eth.personal.sign(signMessage, account, "");
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

            const response = await this.http.get<HelloResponse>("/services/hello",
                { headers: new HttpHeaders(headers) }).toPromise();

            // TODO Set Cookie in the response.
            console.log(response);
            this.auth.depositAddress = response.depositWalletAddress;
            this.auth.bnbBalance = response.bnbBalance;
            this.auth.onlyoneBalance = response.onlyoneBalance;
            this.auth.token = response.token;
            this.auth.web3AccountOnlyoneBalance = response.web3OnlyoneBalance;
        } catch (e: any) {
            this.errorService.showError(e.message);
            console.error(e);
        }
        this.loading.isLoading.next(false);
        this.router.navigate(['']);
    }
}

export interface HelloResponse {
    depositWalletAddress: string;
    bnbBalance: any;
    onlyoneBalance: any;
    token: string;
    web3OnlyoneBalance: any;
}