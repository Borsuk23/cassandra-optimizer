import {Component, Input, OnInit} from '@angular/core';
import {DomSanitizer} from "@angular/platform-browser";
import {MatIconRegistry} from "@angular/material";

@Component({
  selector: 'app-progress-step',
  templateUrl: './progress-step.component.html',
  styleUrls: ['./progress-step.component.css']
})
export class ProgressStepComponent implements OnInit {

  @Input()
  public stepId: String;
  @Input()
  stepText: String;

  status: String = "WAITING";

  constructor(iconRegistry: MatIconRegistry, sanitizer: DomSanitizer) {
    iconRegistry.addSvgIcon(
      'done',
      sanitizer.bypassSecurityTrustResourceUrl('assets/ic_done_black_24px.svg'));
    iconRegistry.addSvgIcon(
      'error',
      sanitizer.bypassSecurityTrustResourceUrl('assets/ic_error_black_24px.svg'));
  }

  ngOnInit() {
  }

  isInProgress() {
    return this.status == "IN_PROGRESS";
  }

  isDone() {
    return this.status == "FINISHED";
  }

  isError() {
    return this.status == "ERROR";
  }

  public updateStatus(value) {
    if (this.status == "WAITING" || this.status == "IN_PROGRESS") {
      this.status = value;
    }
  }

  public reset(){
    this.status="WAITING";
  }

}
