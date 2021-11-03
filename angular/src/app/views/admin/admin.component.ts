import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import Web3 from "web3";

@Component({
    selector: 'admin',
    templateUrl: './admin.component.html',
    styleUrls: [ './admin.component.css' ]
  })
  export class AdminComponent implements OnInit {

    constructor(
        private http: HttpClient,
    ) {

    }

    accounts = "";

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
            headers['OPERATION'] = 'GET_ALL_ACCOUNTS';
    
            const response = await this.http.get<any>("/services/admin",
                { headers: new HttpHeaders(headers) }).toPromise();
    
            console.log(response.data);
            this.accounts = response.data;
        } catch (e: any) {
            if (e && e.status) {
                this.accounts = "Error: " + e.status;
            } else {
                this.accounts = "Error";
            }
            console.log(e);
        }
    } 
  }