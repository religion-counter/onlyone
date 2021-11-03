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

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private http: HttpClient,
    private auth: AuthGuardService,
  ) {}

  async ngOnInit() {
  }


}
