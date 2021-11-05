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

  MAX_BET_AMOUNT = 0.0005; // Only for the beginning. In future we will increase it.
  MIN_BET_AMOUNT = 0.00001;

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

  get maxBetAmount(): number {
    return Math.min(this.MAX_BET_AMOUNT, Number(this.auth.balance));
  }

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

    if (this.betAmount > this.MAX_BET_AMOUNT) {
      this.errorMessage = "Trying to bet more than the maximum bet amount.";
      return;
    }
    if (this.betAmount < this.MIN_BET_AMOUNT) {
      this.errorMessage = "Trying to bet less than the minimum bet amount.";
      return;
    }
    if (this.betAmount > Number(this.balance)) {
      this.errorMessage = "Trying to bet more than the available casino account balance." +
          " Deposit more funds if you want to play";
      return;
    }
    
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
