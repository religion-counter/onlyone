import { Component, OnInit } from '@angular/core';
import { LoadingService } from '../../services/loading/loading.service';

@Component({
  selector: 'loading',
  templateUrl: './loading.component.html',
  styleUrls: ['./loading.component.css']
})
export class LoadingComponent implements OnInit {

  showLoadingSpinner = false;

  constructor(private loaderService: LoadingService) {

    this.loaderService.isLoading.subscribe((value) => {
      this.showLoadingSpinner = value;
    });

  }
  ngOnInit() {
  }

}