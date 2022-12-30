import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'Simple Pools';

  routerLinks = [
    ['/homepage', "Homepage"],
    ['/pools', "Pools"],
  ]

  activatedRoute = this.routerLinks[0][0];

  constructor(
    private router: Router
  ) {}

  ngOnInit() {
  }

  navigate(route: string) {
    this.activatedRoute = route;
    this.router.navigate([route]);
  }

}
