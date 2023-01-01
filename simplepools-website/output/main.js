"use strict";
(self["webpackChunksimplepools"] = self["webpackChunksimplepools"] || []).push([["main"],{

/***/ 5917:
/*!*******************************!*\
  !*** ./src/app/animations.ts ***!
  \*******************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "slideInAnimation": () => (/* binding */ slideInAnimation)
/* harmony export */ });
/* harmony import */ var _angular_animations__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/animations */ 4851);

const slideInAnimation = (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.trigger)('routeAnimations', [(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.transition)('homepage <=> pools', [(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.style)({
  position: 'relative'
}), (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.query)(':enter, :leave', [(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.style)({
  position: 'absolute',
  top: 0,
  left: 0,
  width: '100%'
})], {
  optional: true
}), (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.query)(':enter', [(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.style)({
  left: '-100%'
})], {
  optional: true
}), (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.query)(':leave', (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.animateChild)()), (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.group)([(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.query)(':leave', [(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.animate)('300ms ease-out', (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.style)({
  left: '100%'
}))], {
  optional: true
}), (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.query)(':enter', [(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.animate)('300ms ease-out', (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.style)({
  left: '0%'
}))], {
  optional: true
})])]), (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.transition)('* <=> *', [(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.style)({
  position: 'relative'
}), (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.query)(':enter, :leave', [(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.style)({
  position: 'absolute',
  top: 0,
  left: 0,
  width: '100%'
})], {
  optional: true
}), (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.query)(':enter', [(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.style)({
  left: '-100%'
})], {
  optional: true
}), (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.query)(':leave', (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.animateChild)(), {
  optional: true
}), (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.group)([(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.query)(':leave', [(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.animate)('200ms ease-out', (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.style)({
  left: '100%',
  opacity: 0
}))], {
  optional: true
}), (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.query)(':enter', [(0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.animate)('300ms ease-out', (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.style)({
  left: '0%'
}))], {
  optional: true
}), (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.query)('@*', (0,_angular_animations__WEBPACK_IMPORTED_MODULE_0__.animateChild)(), {
  optional: true
})])])]);

/***/ }),

/***/ 809:
/*!***************************************!*\
  !*** ./src/app/app-routing.module.ts ***!
  \***************************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "AppRoutingModule": () => (/* binding */ AppRoutingModule),
/* harmony export */   "routes": () => (/* binding */ routes)
/* harmony export */ });
/* harmony import */ var _angular_platform_browser__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/platform-browser */ 4497);
/* harmony import */ var _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/platform-browser/animations */ 7146);
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! @angular/router */ 124);
/* harmony import */ var _views_homepage_homepage_component__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./views/homepage/homepage.component */ 5977);
/* harmony import */ var _views_pools_pools_component__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./views/pools/pools.component */ 9211);
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ 2560);







const routes = [{
  path: 'homepage',
  component: _views_homepage_homepage_component__WEBPACK_IMPORTED_MODULE_0__.HomepageComponent,
  data: {
    animation: 'homepage'
  }
}, {
  path: 'pools',
  component: _views_pools_pools_component__WEBPACK_IMPORTED_MODULE_1__.PoolsComponent,
  data: {
    animation: 'pools'
  }
}, {
  path: '**',
  redirectTo: '/homepage',
  data: {
    animation: 'homepage'
  }
}];
class AppRoutingModule {}
AppRoutingModule.ɵfac = function AppRoutingModule_Factory(t) {
  return new (t || AppRoutingModule)();
};
AppRoutingModule.ɵmod = /*@__PURE__*/_angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdefineNgModule"]({
  type: AppRoutingModule
});
AppRoutingModule.ɵinj = /*@__PURE__*/_angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdefineInjector"]({
  imports: [_angular_platform_browser__WEBPACK_IMPORTED_MODULE_3__.BrowserModule, _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_4__.BrowserAnimationsModule, _angular_router__WEBPACK_IMPORTED_MODULE_5__.RouterModule.forRoot(routes), _angular_router__WEBPACK_IMPORTED_MODULE_5__.RouterModule]
});
(function () {
  (typeof ngJitMode === "undefined" || ngJitMode) && _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵsetNgModuleScope"](AppRoutingModule, {
    imports: [_angular_platform_browser__WEBPACK_IMPORTED_MODULE_3__.BrowserModule, _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_4__.BrowserAnimationsModule, _angular_router__WEBPACK_IMPORTED_MODULE_5__.RouterModule],
    exports: [_angular_router__WEBPACK_IMPORTED_MODULE_5__.RouterModule]
  });
})();

/***/ }),

/***/ 721:
/*!**********************************!*\
  !*** ./src/app/app.component.ts ***!
  \**********************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "AppComponent": () => (/* binding */ AppComponent)
/* harmony export */ });
/* harmony import */ var _animations__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./animations */ 5917);
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ 2560);
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/router */ 124);
/* harmony import */ var _components_loading_loading_component__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./components/loading/loading.component */ 7961);




class AppComponent {
  constructor(router, contexts) {
    this.router = router;
    this.contexts = contexts;
    this.title = 'Simple Pools';
    this.routerLinks = [['/homepage', "Homepage"], ['/pools', "Pools"]];
    this.activatedRoute = this.routerLinks[0][0];
  }
  ngOnInit() {}
  navigate(route) {
    this.activatedRoute = route;
    this.router.navigate([route]);
  }
  getRouteAnimationData() {
    return this.contexts.getContext('primary')?.route?.snapshot?.data?.['animation'];
  }
}
AppComponent.ɵfac = function AppComponent_Factory(t) {
  return new (t || AppComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](_angular_router__WEBPACK_IMPORTED_MODULE_3__.Router), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](_angular_router__WEBPACK_IMPORTED_MODULE_3__.ChildrenOutletContexts));
};
AppComponent.ɵcmp = /*@__PURE__*/_angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdefineComponent"]({
  type: AppComponent,
  selectors: [["app-root"]],
  decls: 3,
  vars: 1,
  template: function AppComponent_Template(rf, ctx) {
    if (rf & 1) {
      _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](0, "loading");
      _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](1, "div");
      _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](2, "router-outlet");
      _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
    }
    if (rf & 2) {
      _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](1);
      _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("@routeAnimations", ctx.getRouteAnimationData());
    }
  },
  dependencies: [_angular_router__WEBPACK_IMPORTED_MODULE_3__.RouterOutlet, _components_loading_loading_component__WEBPACK_IMPORTED_MODULE_1__.LoadingComponent],
  styles: ["\n/*# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8uL3NyYy9hcHAvYXBwLmNvbXBvbmVudC5jc3MiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7R0FtQkciLCJzb3VyY2VzQ29udGVudCI6WyIvKiBBcHBDb21wb25lbnQncyBwcml2YXRlIENTUyBzdHlsZXNcclxuaDEge1xyXG4gIG1hcmdpbi1ib3R0b206IDA7XHJcbn1cclxubmF2IGEge1xyXG4gIHBhZGRpbmc6IDFyZW07XHJcbiAgdGV4dC1kZWNvcmF0aW9uOiBub25lO1xyXG4gIG1hcmdpbi10b3A6IDEwcHg7XHJcbiAgZGlzcGxheTogaW5saW5lLWJsb2NrO1xyXG4gIGJhY2tncm91bmQtY29sb3I6ICNlOGU4ZTg7XHJcbiAgY29sb3I6ICMzZDNkM2Q7XHJcbiAgYm9yZGVyLXJhZGl1czogNHB4O1xyXG59XHJcbm5hdiBhOmhvdmVyIHtcclxuICBjb2xvcjogd2hpdGU7XHJcbiAgYmFja2dyb3VuZC1jb2xvcjogIzQyNTQ1QztcclxufVxyXG5uYXYgYS5hY3RpdmUge1xyXG4gIGJhY2tncm91bmQtY29sb3I6IGJsYWNrO1xyXG59ICovIl0sInNvdXJjZVJvb3QiOiIifQ== */"],
  data: {
    animation: [_animations__WEBPACK_IMPORTED_MODULE_0__.slideInAnimation]
  }
});

/***/ }),

/***/ 23:
/*!*******************************!*\
  !*** ./src/app/app.module.ts ***!
  \*******************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "AppModule": () => (/* binding */ AppModule)
/* harmony export */ });
/* harmony import */ var _angular_platform_browser__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! @angular/platform-browser */ 4497);
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(/*! @angular/forms */ 2508);
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! @angular/common/http */ 8987);
/* harmony import */ var _app_routing_module__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./app-routing.module */ 809);
/* harmony import */ var _app_component__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./app.component */ 721);
/* harmony import */ var _angular_material_progress_spinner__WEBPACK_IMPORTED_MODULE_12__ = __webpack_require__(/*! @angular/material/progress-spinner */ 1708);
/* harmony import */ var _angular_material_button__WEBPACK_IMPORTED_MODULE_13__ = __webpack_require__(/*! @angular/material/button */ 4522);
/* harmony import */ var _angular_material_input__WEBPACK_IMPORTED_MODULE_14__ = __webpack_require__(/*! @angular/material/input */ 8562);
/* harmony import */ var _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__(/*! @angular/platform-browser/animations */ 7146);
/* harmony import */ var _components_loading_loading_component__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./components/loading/loading.component */ 7961);
/* harmony import */ var _services_loading_loading_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./services/loading/loading.service */ 3314);
/* harmony import */ var _interceptors_loading_interceptor__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./interceptors/loading-interceptor */ 8943);
/* harmony import */ var _angular_material_tabs__WEBPACK_IMPORTED_MODULE_15__ = __webpack_require__(/*! @angular/material/tabs */ 5892);
/* harmony import */ var _angular_material_dialog__WEBPACK_IMPORTED_MODULE_16__ = __webpack_require__(/*! @angular/material/dialog */ 1484);
/* harmony import */ var _views_pools_pools_component__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ./views/pools/pools.component */ 9211);
/* harmony import */ var _views_homepage_homepage_component__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ./views/homepage/homepage.component */ 5977);
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_17__ = __webpack_require__(/*! @angular/router */ 124);
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! @angular/core */ 2560);



















class AppModule {}
AppModule.ɵfac = function AppModule_Factory(t) {
  return new (t || AppModule)();
};
AppModule.ɵmod = /*@__PURE__*/_angular_core__WEBPACK_IMPORTED_MODULE_7__["ɵɵdefineNgModule"]({
  type: AppModule,
  bootstrap: [_app_component__WEBPACK_IMPORTED_MODULE_1__.AppComponent]
});
AppModule.ɵinj = /*@__PURE__*/_angular_core__WEBPACK_IMPORTED_MODULE_7__["ɵɵdefineInjector"]({
  providers: [_services_loading_loading_service__WEBPACK_IMPORTED_MODULE_3__.LoadingService, {
    provide: _angular_common_http__WEBPACK_IMPORTED_MODULE_8__.HTTP_INTERCEPTORS,
    useClass: _interceptors_loading_interceptor__WEBPACK_IMPORTED_MODULE_4__.LoadingInterceptor,
    multi: true
  }],
  imports: [_angular_platform_browser__WEBPACK_IMPORTED_MODULE_9__.BrowserModule, _angular_forms__WEBPACK_IMPORTED_MODULE_10__.FormsModule, _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_11__.BrowserAnimationsModule, _app_routing_module__WEBPACK_IMPORTED_MODULE_0__.AppRoutingModule, _angular_common_http__WEBPACK_IMPORTED_MODULE_8__.HttpClientModule, _angular_material_progress_spinner__WEBPACK_IMPORTED_MODULE_12__.MatProgressSpinnerModule, _angular_material_button__WEBPACK_IMPORTED_MODULE_13__.MatButtonModule, _angular_material_input__WEBPACK_IMPORTED_MODULE_14__.MatInputModule, _angular_material_tabs__WEBPACK_IMPORTED_MODULE_15__.MatTabsModule, _angular_material_dialog__WEBPACK_IMPORTED_MODULE_16__.MatDialogModule, _angular_platform_browser__WEBPACK_IMPORTED_MODULE_9__.BrowserModule, _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_11__.BrowserAnimationsModule, _angular_router__WEBPACK_IMPORTED_MODULE_17__.RouterModule.forRoot(_app_routing_module__WEBPACK_IMPORTED_MODULE_0__.routes)
  // The HttpClientInMemoryWebApiModule module intercepts HTTP requests
  // and returns simulated server responses.
  // Remove it when a real server is ready to receive requests.
  // HttpClientInMemoryWebApiModule.forRoot(
  //   InMemoryDataService, { dataEncapsulation: false }
  // )
  ]
});

(function () {
  (typeof ngJitMode === "undefined" || ngJitMode) && _angular_core__WEBPACK_IMPORTED_MODULE_7__["ɵɵsetNgModuleScope"](AppModule, {
    declarations: [_app_component__WEBPACK_IMPORTED_MODULE_1__.AppComponent, _components_loading_loading_component__WEBPACK_IMPORTED_MODULE_2__.LoadingComponent, _views_homepage_homepage_component__WEBPACK_IMPORTED_MODULE_6__.HomepageComponent, _views_pools_pools_component__WEBPACK_IMPORTED_MODULE_5__.PoolsComponent],
    imports: [_angular_platform_browser__WEBPACK_IMPORTED_MODULE_9__.BrowserModule, _angular_forms__WEBPACK_IMPORTED_MODULE_10__.FormsModule, _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_11__.BrowserAnimationsModule, _app_routing_module__WEBPACK_IMPORTED_MODULE_0__.AppRoutingModule, _angular_common_http__WEBPACK_IMPORTED_MODULE_8__.HttpClientModule, _angular_material_progress_spinner__WEBPACK_IMPORTED_MODULE_12__.MatProgressSpinnerModule, _angular_material_button__WEBPACK_IMPORTED_MODULE_13__.MatButtonModule, _angular_material_input__WEBPACK_IMPORTED_MODULE_14__.MatInputModule, _angular_material_tabs__WEBPACK_IMPORTED_MODULE_15__.MatTabsModule, _angular_material_dialog__WEBPACK_IMPORTED_MODULE_16__.MatDialogModule, _angular_platform_browser__WEBPACK_IMPORTED_MODULE_9__.BrowserModule, _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_11__.BrowserAnimationsModule, _angular_router__WEBPACK_IMPORTED_MODULE_17__.RouterModule]
  });
})();

/***/ }),

/***/ 7961:
/*!*********************************************************!*\
  !*** ./src/app/components/loading/loading.component.ts ***!
  \*********************************************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "LoadingComponent": () => (/* binding */ LoadingComponent)
/* harmony export */ });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ 2560);
/* harmony import */ var _services_loading_loading_service__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../../services/loading/loading.service */ 3314);
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common */ 4666);
/* harmony import */ var _angular_material_progress_spinner__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/material/progress-spinner */ 1708);




function LoadingComponent_mat_spinner_0_Template(rf, ctx) {
  if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](0, "mat-spinner", 1);
  }
}
class LoadingComponent {
  constructor(loaderService) {
    this.loaderService = loaderService;
    this.showLoadingSpinner = false;
    this.loaderService.isLoading.subscribe(value => {
      this.showLoadingSpinner = value;
    });
  }
  ngOnInit() {}
}
LoadingComponent.ɵfac = function LoadingComponent_Factory(t) {
  return new (t || LoadingComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](_services_loading_loading_service__WEBPACK_IMPORTED_MODULE_0__.LoadingService));
};
LoadingComponent.ɵcmp = /*@__PURE__*/_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineComponent"]({
  type: LoadingComponent,
  selectors: [["loading"]],
  decls: 1,
  vars: 1,
  consts: [["class", "spinner", 4, "ngIf"], [1, "spinner"]],
  template: function LoadingComponent_Template(rf, ctx) {
    if (rf & 1) {
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtemplate"](0, LoadingComponent_mat_spinner_0_Template, 1, 0, "mat-spinner", 0);
    }
    if (rf & 2) {
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("ngIf", ctx.showLoadingSpinner);
    }
  },
  dependencies: [_angular_common__WEBPACK_IMPORTED_MODULE_2__.NgIf, _angular_material_progress_spinner__WEBPACK_IMPORTED_MODULE_3__.MatProgressSpinner],
  styles: [".spinner[_ngcontent-%COMP%] {\r\n    position: fixed;\r\n    margin: auto;\r\n    width: 100% !important;\r\n    height: 100% !important;\r\n    top:0;\r\n    bottom: 0;\r\n    left: 0;\r\n    right: 0;\r\n    z-index: 1;\r\n    background-color: rgba(0, 0, 0, 0.3);\r\n  }\r\n  \r\n    .mat-progress-spinner svg {\r\n    top: 0;\r\n    bottom: 0;\r\n    left: 0;\r\n    right: 0;\r\n    margin: auto;\r\n    width: 200px;\r\n    height: 200px;\r\n  }\n/*# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8uL3NyYy9hcHAvY29tcG9uZW50cy9sb2FkaW5nL2xvYWRpbmcuY29tcG9uZW50LmNzcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtJQUNJLGVBQWU7SUFDZixZQUFZO0lBQ1osc0JBQXNCO0lBQ3RCLHVCQUF1QjtJQUN2QixLQUFLO0lBQ0wsU0FBUztJQUNULE9BQU87SUFDUCxRQUFRO0lBQ1IsVUFBVTtJQUNWLG9DQUFvQztFQUN0Qzs7RUFFQTtJQUNFLE1BQU07SUFDTixTQUFTO0lBQ1QsT0FBTztJQUNQLFFBQVE7SUFDUixZQUFZO0lBQ1osWUFBWTtJQUNaLGFBQWE7RUFDZiIsInNvdXJjZXNDb250ZW50IjpbIi5zcGlubmVyIHtcclxuICAgIHBvc2l0aW9uOiBmaXhlZDtcclxuICAgIG1hcmdpbjogYXV0bztcclxuICAgIHdpZHRoOiAxMDAlICFpbXBvcnRhbnQ7XHJcbiAgICBoZWlnaHQ6IDEwMCUgIWltcG9ydGFudDtcclxuICAgIHRvcDowO1xyXG4gICAgYm90dG9tOiAwO1xyXG4gICAgbGVmdDogMDtcclxuICAgIHJpZ2h0OiAwO1xyXG4gICAgei1pbmRleDogMTtcclxuICAgIGJhY2tncm91bmQtY29sb3I6IHJnYmEoMCwgMCwgMCwgMC4zKTtcclxuICB9XHJcbiAgXHJcbiAgOjpuZy1kZWVwIC5tYXQtcHJvZ3Jlc3Mtc3Bpbm5lciBzdmcge1xyXG4gICAgdG9wOiAwO1xyXG4gICAgYm90dG9tOiAwO1xyXG4gICAgbGVmdDogMDtcclxuICAgIHJpZ2h0OiAwO1xyXG4gICAgbWFyZ2luOiBhdXRvO1xyXG4gICAgd2lkdGg6IDIwMHB4O1xyXG4gICAgaGVpZ2h0OiAyMDBweDtcclxuICB9XHJcbiAgIl0sInNvdXJjZVJvb3QiOiIifQ== */"]
});

/***/ }),

/***/ 8943:
/*!*****************************************************!*\
  !*** ./src/app/interceptors/loading-interceptor.ts ***!
  \*****************************************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "LoadingInterceptor": () => (/* binding */ LoadingInterceptor)
/* harmony export */ });
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common/http */ 8987);
/* harmony import */ var rxjs__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! rxjs */ 2378);
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/core */ 2560);
/* harmony import */ var _services_loading_loading_service__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../services/loading/loading.service */ 3314);




class LoadingInterceptor {
  constructor(loadingService) {
    this.loadingService = loadingService;
    this.requests = [];
  }
  removeRequest(req) {
    const i = this.requests.indexOf(req);
    if (i >= 0) {
      this.requests.splice(i, 1);
    }
    this.loadingService.isLoading.next(this.requests.length > 0);
  }
  intercept(req, next) {
    this.requests.push(req);
    console.log("No of requests--->" + this.requests.length);
    this.loadingService.isLoading.next(true);
    return new rxjs__WEBPACK_IMPORTED_MODULE_1__.Observable(observer => {
      const subscription = next.handle(req).subscribe(event => {
        if (event instanceof _angular_common_http__WEBPACK_IMPORTED_MODULE_2__.HttpResponse) {
          this.removeRequest(req);
          observer.next(event);
        }
      }, err => {
        this.removeRequest(req);
        observer.error(err);
      }, () => {
        this.removeRequest(req);
        observer.complete();
      });
      // remove request from queue when cancelled
      return () => {
        this.removeRequest(req);
        subscription.unsubscribe();
      };
    });
  }
}
LoadingInterceptor.ɵfac = function LoadingInterceptor_Factory(t) {
  return new (t || LoadingInterceptor)(_angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵinject"](_services_loading_loading_service__WEBPACK_IMPORTED_MODULE_0__.LoadingService));
};
LoadingInterceptor.ɵprov = /*@__PURE__*/_angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdefineInjectable"]({
  token: LoadingInterceptor,
  factory: LoadingInterceptor.ɵfac
});

/***/ }),

/***/ 3314:
/*!*****************************************************!*\
  !*** ./src/app/services/loading/loading.service.ts ***!
  \*****************************************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "LoadingService": () => (/* binding */ LoadingService)
/* harmony export */ });
/* harmony import */ var rxjs__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! rxjs */ 4505);
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ 2560);


class LoadingService {
  constructor() {
    this.isLoading = new rxjs__WEBPACK_IMPORTED_MODULE_0__.BehaviorSubject(false);
  }
}
LoadingService.ɵfac = function LoadingService_Factory(t) {
  return new (t || LoadingService)();
};
LoadingService.ɵprov = /*@__PURE__*/_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineInjectable"]({
  token: LoadingService,
  factory: LoadingService.ɵfac,
  providedIn: 'root'
});

/***/ }),

/***/ 5977:
/*!******************************************************!*\
  !*** ./src/app/views/homepage/homepage.component.ts ***!
  \******************************************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "HomepageComponent": () => (/* binding */ HomepageComponent)
/* harmony export */ });
/* harmony import */ var C_dev_src_oo_defiswap_git_onlyone_simplepools_website_node_modules_babel_runtime_helpers_esm_asyncToGenerator_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./node_modules/@babel/runtime/helpers/esm/asyncToGenerator.js */ 1670);
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ 2560);
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/router */ 124);



class HomepageComponent {
  constructor(router) {
    this.router = router;
  }
  ngOnInit() {
    return (0,C_dev_src_oo_defiswap_git_onlyone_simplepools_website_node_modules_babel_runtime_helpers_esm_asyncToGenerator_js__WEBPACK_IMPORTED_MODULE_0__["default"])(function* () {})();
  }
  showPools() {
    this.router.navigateByUrl('pools');
  }
}
HomepageComponent.ɵfac = function HomepageComponent_Factory(t) {
  return new (t || HomepageComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](_angular_router__WEBPACK_IMPORTED_MODULE_2__.Router));
};
HomepageComponent.ɵcmp = /*@__PURE__*/_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineComponent"]({
  type: HomepageComponent,
  selectors: [["homepage"]],
  decls: 12,
  vars: 0,
  consts: [["viewBox", "0 0 500 160", 1, "simple-pools"], ["id", "curve", "d", "M73.2,148.6c4-6.1,65.5-96.8,178.6-95.6c111.3,1.2,170.8,90.3,175.1,97"], [1, "simple-pools"], [0, "xlink", "href", "#curve"], [1, "slogan"], ["href", "https://github.com/religion-counter/onlyone/blob/main/smart-contracts/SimplePools.sol"], ["width", "20px", "src", "./assets/github.svg", 2, "background-color", "white"], [1, "show-pools"], [3, "click"]],
  template: function HomepageComponent_Template(rf, ctx) {
    if (rf & 1) {
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵnamespaceSVG"]();
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](0, "svg", 0);
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](1, "path", 1);
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](2, "text", 2)(3, "textPath", 3);
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](4, " Simple Pools ");
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]()()();
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵnamespaceHTML"]();
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](5, "div", 4);
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](6, " DeFi pools with simple code and zero tax. ");
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](7, "a", 5);
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](8, "img", 6);
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]()();
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](9, "div", 7)(10, "button", 8);
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("click", function HomepageComponent_Template_button_click_10_listener() {
        return ctx.showPools();
      });
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](11, "Show Pools");
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]()();
    }
  },
  styles: ["path[_ngcontent-%COMP%] {\r\n    fill: transparent;\r\n}\r\n\r\n.simple-pools[_ngcontent-%COMP%]   text[_ngcontent-%COMP%] {\r\n    fill: #FF9800;\r\n    font-size: 70px;\r\n}\r\n\r\n.slogan[_ngcontent-%COMP%] {\r\n    color: #FF9800;\r\n    font-size: 30px;\r\n    text-align: center;\r\n}\r\n\r\n.show-pools[_ngcontent-%COMP%] {\r\n    margin-top: 8px;\r\n    display: flex;\r\n    justify-content: center;\r\n    align-items: center;\r\n}\n/*# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8uL3NyYy9hcHAvdmlld3MvaG9tZXBhZ2UvaG9tZXBhZ2UuY29tcG9uZW50LmNzcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtJQUNJLGlCQUFpQjtBQUNyQjs7QUFFQTtJQUNJLGFBQWE7SUFDYixlQUFlO0FBQ25COztBQUVBO0lBQ0ksY0FBYztJQUNkLGVBQWU7SUFDZixrQkFBa0I7QUFDdEI7O0FBRUE7SUFDSSxlQUFlO0lBQ2YsYUFBYTtJQUNiLHVCQUF1QjtJQUN2QixtQkFBbUI7QUFDdkIiLCJzb3VyY2VzQ29udGVudCI6WyJwYXRoIHtcclxuICAgIGZpbGw6IHRyYW5zcGFyZW50O1xyXG59XHJcblxyXG4uc2ltcGxlLXBvb2xzIHRleHQge1xyXG4gICAgZmlsbDogI0ZGOTgwMDtcclxuICAgIGZvbnQtc2l6ZTogNzBweDtcclxufVxyXG5cclxuLnNsb2dhbiB7XHJcbiAgICBjb2xvcjogI0ZGOTgwMDtcclxuICAgIGZvbnQtc2l6ZTogMzBweDtcclxuICAgIHRleHQtYWxpZ246IGNlbnRlcjtcclxufVxyXG5cclxuLnNob3ctcG9vbHMge1xyXG4gICAgbWFyZ2luLXRvcDogOHB4O1xyXG4gICAgZGlzcGxheTogZmxleDtcclxuICAgIGp1c3RpZnktY29udGVudDogY2VudGVyO1xyXG4gICAgYWxpZ24taXRlbXM6IGNlbnRlcjtcclxufSJdLCJzb3VyY2VSb290IjoiIn0= */"]
});

/***/ }),

/***/ 9211:
/*!************************************************!*\
  !*** ./src/app/views/pools/pools.component.ts ***!
  \************************************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "PoolsComponent": () => (/* binding */ PoolsComponent)
/* harmony export */ });
/* harmony import */ var C_dev_src_oo_defiswap_git_onlyone_simplepools_website_node_modules_babel_runtime_helpers_esm_asyncToGenerator_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./node_modules/@babel/runtime/helpers/esm/asyncToGenerator.js */ 1670);
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ 2560);
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/router */ 124);



const _c0 = function () {
  return ["/homepage"];
};
class PoolsComponent {
  constructor() {}
  ngOnInit() {
    return (0,C_dev_src_oo_defiswap_git_onlyone_simplepools_website_node_modules_babel_runtime_helpers_esm_asyncToGenerator_js__WEBPACK_IMPORTED_MODULE_0__["default"])(function* () {})();
  }
  connectWallet() {
    console.log("connect wallet...");
  }
}
PoolsComponent.ɵfac = function PoolsComponent_Factory(t) {
  return new (t || PoolsComponent)();
};
PoolsComponent.ɵcmp = /*@__PURE__*/_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineComponent"]({
  type: PoolsComponent,
  selectors: [["pools"]],
  decls: 4,
  vars: 2,
  consts: [[3, "routerLink"], ["src", "../../../assets/simplepools-logo.png", "height", "50"], [1, "connect-wallet-button", 3, "click"]],
  template: function PoolsComponent_Template(rf, ctx) {
    if (rf & 1) {
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](0, "a", 0);
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](1, "img", 1);
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](2, "button", 2);
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("click", function PoolsComponent_Template_button_click_2_listener() {
        return ctx.connectWallet();
      });
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](3, "Connect Wallet");
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    }
    if (rf & 2) {
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("routerLink", _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵpureFunction0"](1, _c0));
    }
  },
  dependencies: [_angular_router__WEBPACK_IMPORTED_MODULE_2__.RouterLink],
  styles: [".connect-wallet-button[_ngcontent-%COMP%] {\r\n    position: absolute;\r\n    right: 0;\r\n}\n/*# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8uL3NyYy9hcHAvdmlld3MvcG9vbHMvcG9vbHMuY29tcG9uZW50LmNzcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtJQUNJLGtCQUFrQjtJQUNsQixRQUFRO0FBQ1oiLCJzb3VyY2VzQ29udGVudCI6WyIuY29ubmVjdC13YWxsZXQtYnV0dG9uIHtcclxuICAgIHBvc2l0aW9uOiBhYnNvbHV0ZTtcclxuICAgIHJpZ2h0OiAwO1xyXG59XHJcblxyXG4iXSwic291cmNlUm9vdCI6IiJ9 */"]
});

/***/ }),

/***/ 4766:
/*!*****************************************!*\
  !*** ./src/environments/environment.ts ***!
  \*****************************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "environment": () => (/* binding */ environment)
/* harmony export */ });
// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
const environment = {
  production: false
};
/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.

/***/ }),

/***/ 8835:
/*!*********************!*\
  !*** ./src/main.ts ***!
  \*********************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _angular_platform_browser__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/platform-browser */ 4497);
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ 2560);
/* harmony import */ var _app_app_module__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./app/app.module */ 23);
/* harmony import */ var _environments_environment__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./environments/environment */ 4766);




if (_environments_environment__WEBPACK_IMPORTED_MODULE_1__.environment.production) {
  (0,_angular_core__WEBPACK_IMPORTED_MODULE_2__.enableProdMode)();
}
_angular_platform_browser__WEBPACK_IMPORTED_MODULE_3__.platformBrowser().bootstrapModule(_app_app_module__WEBPACK_IMPORTED_MODULE_0__.AppModule).catch(err => console.error(err));

/***/ })

},
/******/ __webpack_require__ => { // webpackRuntimeModules
/******/ var __webpack_exec__ = (moduleId) => (__webpack_require__(__webpack_require__.s = moduleId))
/******/ __webpack_require__.O(0, ["vendor"], () => (__webpack_exec__(8835)));
/******/ var __webpack_exports__ = __webpack_require__.O();
/******/ }
]);
//# sourceMappingURL=main.js.map