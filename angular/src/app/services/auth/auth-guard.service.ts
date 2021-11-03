import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import Web3 from 'web3';

@Injectable({ providedIn: 'root' })
export class AuthGuardService implements CanActivate {

  constructor(public router: Router) {}

  token = "";
  wallet = "";
  balance = "";
  depositAddress = "";
  web3: Web3 | undefined = undefined;
  web3AccountBalance: number = 0;

  canActivate(): boolean {
    if (this.token.length == 0 ) {
        this.router.navigate(['login']);
        return false;
    }
    return true;
  }
}
