import { Location } from "@angular/common";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { __core_private_testing_placeholder__ } from "@angular/core/testing";
import { ActivatedRoute, Router } from "@angular/router";
import { ErrorService } from "src/app/services/error/error.service";
import { environment } from "src/environments/environment";
import Web3 from "web3";
import { LoadingService } from "../../services/loading/loading.service";
import * as ethers from "ethers"

@Component({
  selector: 'pools',
  templateUrl: './pools.component.html',
  styleUrls: [ './pools.component.css' ]
})
export class PoolsComponent implements OnInit {

    constructor(
    ){
    }

    async ngOnInit( ) {
    }

    connectWallet() {
        console.log("connect wallet...");
    }

}

