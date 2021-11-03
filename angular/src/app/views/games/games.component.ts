import { Location } from "@angular/common";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { AuthGuardService } from "../../services/auth/auth-guard.service";

@Component({
  selector: 'games',
  templateUrl: './games.component.html',
  styleUrls: [ './games.component.css' ]
})
export class GamesComponent implements OnInit {

  ONE_TO_TWENTY = "ONE_TO_TWENTY";
  TWENTY_ONE_TO_FORTY = "TWENTY_ONE_TO_FORTY";
  ODDS = "ODDS";
  EVENS = "EVENS";
  NUMBER_PREFIX = "NUMBER_";

  lastChosen = "";
  lastWin = "";
  errorMessage = "";

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private http: HttpClient,
    private auth: AuthGuardService,
  ) {}

  selectedBet = '';
  betAmount = 0;

  get balance(): string {
    return this.auth.balance;
  }

  async ngOnInit() {

  }

  getStyle(button: string) {
    if (button == this.selectedBet) {
      return {
        'backgroundColor': 'black',
        'color': 'white',
      };
    }
    return {};
  }

  disabledButton(bet: string): boolean {
    return this.selectedBet != '' && bet != this.selectedBet;
  }

  oneToTwenty() {
    this.selectedBet = this.ONE_TO_TWENTY;
  }

  twentyOneToForty() {
    this.selectedBet = this.TWENTY_ONE_TO_FORTY;
  }

  odds() {
    this.selectedBet = this.ODDS;
  }

  evens() {
    this.selectedBet = this.EVENS;
  }

  singleNumber(i: number) {
    console.log("Bet on : " + i);
    this.selectedBet = this.NUMBER_PREFIX + i;
  }

  async startGame() {
    
    const headers: any = {
      WALLET: this.auth.wallet,
      BET_AMOUNT: this.betAmount.toString(),
      TOKEN: this.auth.token,
      SELECTED_BET: this.selectedBet,
    };

    let response = undefined;
    try {
      const res = await this.http.get<any>("/services/forty",
        { headers: new HttpHeaders(headers)} ).toPromise();
      response = res.data.split(':');
    } catch (e: any) {
       console.log("Error playing forty game.");
       console.error(e);
       this.errorMessage = e;
       this.lastChosen = '';
       this.lastWin = "0";
       return;
    }
    this.lastChosen = response[0];
    this.auth.balance = response[1];
    this.lastWin = response[2];
  }
}
