import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ProgressStepComponent} from "../progress-step/progress-step.component";

@Component({
  selector: 'app-progress-bar',
  templateUrl: './progress-bar.component.html',
  styleUrls: ['./progress-bar.component.css']
})
export class ProgressBarComponent implements OnInit {

  errorMessage: String;
  status: String;
  steps = [
    {stepID: "parse", stepText: "Parsowanie danych", begin: "PARSING_INPUT", end: "INPUT_PARSED"},
    {
      stepID: "generate",
      stepText: "Generowanie projekcji",
      begin: "GENERATING_PROJECTIONS",
      end: "PROJECTIONS_GENERATED"
    },
    {stepID: "merge", stepText: "Łączenie projekcji", begin: "MERGING_PROJECTIONS", end: "PROJECTIONS_MERGED"},
    {
      stepID: "prioritize",
      stepText: "Ocena projekcji",
      begin: "PRIORITIZING_PROJECTIONS",
      end: "PROJECTIONS_PRIORITIZED"
    },
    {stepID: "benchmark", stepText: "Testy na klastrze Cassandry", begin: "BENCHMARKING", end: "FINISHED"}
  ];

  @ViewChildren(ProgressStepComponent) processSteps: QueryList<ProgressStepComponent>;

  constructor() {
  }

  ngOnInit() {
  }

  isError()
  {
    return this.status =="ERROR";
  }
  public updateStatuses(substatuses: String[], status: String, errorMessage: String) {
    console.log('test');
    if (status == "ERROR") {
      this.processSteps.forEach(processStep => {
        this.steps.forEach(step => {
          if (processStep.stepId == step.stepID) {
            substatuses.forEach(substatus => {
              if (substatus == step.end) {
                processStep.updateStatus("FINISHED");
              }
            });
          }
        });
      });

      this.processSteps.forEach(processStep => {
        processStep.updateStatus("ERROR");
      });
      this.errorMessage = errorMessage;
      this.status="ERROR";
    }
    if (status == "FINISHED") {
      this.processSteps.forEach(processStep => {
        processStep.updateStatus("FINISHED");
      });
      this.status="FINISHED";
    }
    if (status == "IN_PROGRESS") {
      this.status="IN_PROGRESS";
      this.processSteps.forEach(processStep => {
        this.steps.forEach(step => {
          if (processStep.stepId == step.stepID) {
            substatuses.forEach(substatus => {
              if (substatus == step.begin) {
                processStep.updateStatus("IN_PROGRESS");
              } else if (substatus == step.end) {
                processStep.updateStatus("FINISHED");
              }
            });
          }
        });
      });
    }
  }

  reset(){
    this.processSteps.forEach(processStep=> {
      processStep.reset();
    });
  }
}
