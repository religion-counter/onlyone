import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AccountComponent } from './views/account/account.component';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './views/login/login.component';
import { AdminComponent } from './views/admin/admin.component';
import { LoadingComponent } from './components/loading/loading.component';
import { LoadingService } from './services/loading/loading.service';
import { LoadingInterceptor } from './interceptors/loading-interceptor';
import { GamesComponent } from './views/games/games.component';
import { MatTabsModule } from '@angular/material/tabs';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    MatInputModule,
    MatTabsModule,

    // The HttpClientInMemoryWebApiModule module intercepts HTTP requests
    // and returns simulated server responses.
    // Remove it when a real server is ready to receive requests.
    // HttpClientInMemoryWebApiModule.forRoot(
    //   InMemoryDataService, { dataEncapsulation: false }
    // )
  ],
  declarations: [
    AppComponent,
    AccountComponent,
    LoginComponent,
    AdminComponent,
    LoadingComponent,
    GamesComponent,
  ],
  bootstrap: [ AppComponent ],
  providers: [
    LoadingService,
    { provide: HTTP_INTERCEPTORS, useClass: LoadingInterceptor, multi: true }
  ]
})
export class AppModule { }
