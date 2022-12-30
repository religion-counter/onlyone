import { Location } from "@angular/common";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

@Component({
  selector: 'homepage',
  templateUrl: './homepage.component.html',
  styleUrls: [ './homepage.component.css' ]
})
export class HomepageComponent implements OnInit {

    constructor(
        private router: Router
    ){
    }

    async ngOnInit( ) {
    }

    showPools() {
        this.router.navigateByUrl('pools');
    }
}
