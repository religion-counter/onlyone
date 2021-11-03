import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthGuardService } from './services/auth/auth-guard.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  routerLinks = [
    ['/account', "Account"],
    ['/games', "Games"],
  ]

  activatedRoute = this.routerLinks[0][0];

  constructor(
    private auth: AuthGuardService,
    private router: Router
  ) {}

  title = 'Onlyone Casino';

  get loggedIn(): boolean {
    return this.auth.token != null && this.auth.token.length > 0;
  }

  navigate(route: string) {
    this.activatedRoute = route;
    this.router.navigate([route]);
  }

}
