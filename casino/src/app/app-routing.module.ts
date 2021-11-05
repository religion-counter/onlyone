import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AccountComponent } from './views/account/account.component';
import { LoginComponent } from './views/login/login.component';
import { AuthGuardService } from './services/auth/auth-guard.service';
import { AdminComponent } from './views/admin/admin.component';
import { GamesComponent } from './views/games/games.component';

const routes: Routes = [
  { path: '', redirectTo: '/account', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'account', 
    component: AccountComponent,
    canActivate: [AuthGuardService] 
  },
  { path: 'games', 
    component: GamesComponent,
    canActivate: [AuthGuardService] 
  },

  // ONLY FOR DEV
  { path: 'admin', component: AdminComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
