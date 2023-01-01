import { Injectable } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { ErrorDialog } from "src/app/components/error-dialog/error-dialog.component";

@Injectable({ providedIn: 'root' })
export class ErrorService {

    constructor(public dialog: MatDialog) {}

    showError(errorMessage: string): void {
        console.error(errorMessage);
        const errorDialogRef = this.dialog.open(ErrorDialog, {
            data: {
                errorMessage: errorMessage
            },
        });
    
        errorDialogRef.afterClosed().subscribe(result => {
          console.log('The error dialog was closed: ' + result);
        });
      }
}

export interface ErrorDialogData {
    errorMessage: string;
}