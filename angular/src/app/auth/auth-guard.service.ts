import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthGuardService implements CanActivate {

  constructor(public router: Router) {}

  token = "";
  wallet = "";
  balance = "";
  depositAddress = "";

  canActivate(): boolean {
    if (this.token.length == 0 ) {
        this.router.navigate(['login']);
        return false;
    }
    return true;
  }
}
