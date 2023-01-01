import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { ErrorDialogData } from "src/app/services/error/error.service";

@Component({
    selector: 'error-dialog',
    templateUrl: './error-dialog.component.html',
    styleUrls: ['./error-dialog.component.css']
})
export class ErrorDialog {

    constructor(
      public dialogRef: MatDialogRef<ErrorDialog>,
      @Inject(MAT_DIALOG_DATA) public data: ErrorDialogData,
    ) {}

    
}