import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './views/homepage/homepage.component';
import { PoolsComponent } from './views/pools/pools.component';


export const routes: Routes = [
  { 
    path: 'homepage', 
    component: HomepageComponent,
    data: {
      animation: 'homepage',
    }
  },
  { 
    path: 'pools', 
    component: PoolsComponent,
    data: {
      animation: 'pools',
    }
  },
  { 
    path: '**', 
    redirectTo: '/homepage',
    data: {
      animation: 'homepage',
    }
  },
];

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(routes)
  ],
  exports: [ 
    RouterModule
  ]
})
export class AppRoutingModule {}
