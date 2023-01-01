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
  MIN_ONLYONE_BET_AMOUNT = 0.000000001;
  MAX_BET_ONLYONE = 0.00001333;

  lastChosen = "";
  lastOnlyoneChosen = ""
  lastWin = "";
  lastOnlyoneWin = "";
  errorMessage = "";
  showLostMessage = false;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private http: HttpClient,
    private auth: AuthGuardService,
  ) {}

  selectedBet = '';
  betAmount = 0;
  betOnlyoneAmount = 0;

  get maxBetAmount(): number {
    return Math.min(this.MAX_BET_AMOUNT, Number(this.auth.bnbBalance));
  }

  get maxOnlyoneBetAmount(): number {
    return Math.min(this.MAX_BET_ONLYONE, Number(this.auth.onlyoneBalance));
  }


  get balance(): number | undefined {
    return this.auth.bnbBalance;
  }

  get onlyoneBalance(): number | undefined {
    return this.auth.onlyoneBalance;
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

  async startOnlyoneGame() {

    if (this.selectedBet === '') {
      this.errorMessage = "Please select a bet option";
      return;
    }

    if (this.betOnlyoneAmount > this.MAX_BET_ONLYONE) {
      this.errorMessage = "Trying to bet more than the maximum bet amount.";
      return;
    }
    if (this.betOnlyoneAmount < this.MIN_BET_AMOUNT) {
      this.errorMessage = "Trying to bet less than the minimum bet amount.";
      return;
    }
    if (this.betOnlyoneAmount > Number(this.onlyoneBalance)) {
      this.errorMessage = "Trying to bet more than the available casino account balance." +
          " Deposit more funds if you want to play";
      return;
    }
    
    const headers: any = {
      WALLET: this.auth.accountAddress,
      BET_AMOUNT: this.betOnlyoneAmount.toString(),
      TOKEN: this.auth.token,
      SELECTED_BET: this.selectedBet,
    };

    let response = undefined;
    try {
      const res = await this.http.get<any>("/services/forty-onlyone/",
        { headers: new HttpHeaders(headers)} ).toPromise();
      response = res.split(':');
    } catch (e: any) {
       console.log("Error playing forty game.");
       console.error(e);
       if (e.message) {
         this.errorMessage = e.message;
       } else {
         this.errorMessage = e;
       }
       
       this.lastOnlyoneChosen = '';
       this.lastOnlyoneWin = "0";
       return;
    }
    this.errorMessage = '';
    this.lastOnlyoneChosen = response[0];
    this.auth.onlyoneBalance = response[1];
    this.lastOnlyoneWin = response[2];
    if (parseFloat(response[2]) < 1e-18) {
      this.showLostMessage = true;
    } else {
      this.showLostMessage = false;
    }
  }

  async startGame() {

    this.showLostMessage = false;
    if (this.selectedBet === '') {
      this.errorMessage = "Please select a bet option";
      return;
    }

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
      WALLET: this.auth.accountAddress,
      BET_AMOUNT: this.betAmount.toString(),
      TOKEN: this.auth.token,
      SELECTED_BET: this.selectedBet,
    };

    let response = undefined;
    try {
      const res = await this.http.get<any>("/services/forty",
        { headers: new HttpHeaders(headers)} ).toPromise();
      response = res.split(':');
    } catch (e: any) {
       console.log("Error playing forty game.");
       console.error(e);
       if (e.message) {
         this.errorMessage = e.message;
       } else {
         this.errorMessage = e;
       }
       
       this.lastChosen = '';
       this.lastWin = "0";
       return;
    }
    this.errorMessage = '';
    this.lastChosen = response[0];
    this.auth.bnbBalance = response[1];
    this.lastWin = response[2];
    this.showLostMessage = false;
  }
}
