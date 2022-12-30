import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './views/homepage/homepage.component';
import { PoolsComponent } from './views/pools/pools.component';


const routes: Routes = [
  { path: 'homepage', component: HomepageComponent },
  { path: 'pools', component: PoolsComponent },
  { path: '**', redirectTo: '/homepage' },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
