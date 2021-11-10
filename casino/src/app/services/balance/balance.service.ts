import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import Web3 from 'web3';
import { AuthGuardService } from '../auth/auth-guard.service';
import { ErrorService } from '../error/error.service';

@Injectable({ providedIn: 'root' })
export class BalanceService {

  constructor(
      public http: HttpClient,
      public auth: AuthGuardService,
      public errorService: ErrorService,
    ) {}

  async refreshBalance(): Promise<void> {
    // Set loading indicator to get balance button...
    try {
        const headers: any = {
            WALLET: this.auth.accountAddress,
            TOKEN: this.auth.token,
        }
        const resp = await this.http.get<BalanceResponse>("/services/balance",
            { headers: new HttpHeaders(headers) }
        ).toPromise();

        this.auth.bnbBalance = resp.bnbBalance;
        this.auth.onlyoneBalance = resp.onlyoneBalance;
        // Set timeout to reset the loading indicator after 1 minute...
    } catch (e: any) {
        this.errorService.showError("Couldn't get balance: " + e.message);
    }
  }
}

export interface BalanceResponse {
    bnbBalance: number;
    onlyoneBalance: number;
}
