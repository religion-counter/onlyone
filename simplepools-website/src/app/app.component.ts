import { Component, OnInit } from '@angular/core';
import { ChildrenOutletContexts, Router } from '@angular/router';
import { slideInAnimation } from './animations';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    slideInAnimation,
  ]
})
export class AppComponent implements OnInit {

  title = 'Simple Pools';

  routerLinks = [
    ['/homepage', "Homepage"],
    ['/pools', "Pools"],
  ]

  activatedRoute = this.routerLinks[0][0];

  constructor(
    private router: Router,
    private contexts: ChildrenOutletContexts,
  ) {}

  ngOnInit() {
  }

  navigate(route: string) {
    this.activatedRoute = route;
    this.router.navigate([route]);
  }

  getRouteAnimationData() {
    return this.contexts.getContext('primary')?.route?.snapshot?.data?.['animation'];
  }

}
