import {Component, OnInit, QueryList, ViewChild} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import {QueryListComponent} from "../query-list/query-list.component";
import {TableListComponent} from "../table-list/table-list.component";
import {ProgressBarComponent} from "../progress-bar/progress-bar.component";
import {BenchmarkResultComponent} from "../benchmark-result/benchmark-result.component";

@Component({
  selector: 'app-optimize-form',
  templateUrl: './optimize-form.component.html',
  styleUrls: ['./optimize-form.component.css']
})
export class OptimizeFormComponent implements OnInit {
  status:String = "NEW";
  substatuses:String[] = [];
  process:String = "test";
  intervalId;
  results = [];
  @ViewChild(QueryListComponent) queries:QueryListComponent;
  @ViewChild(TableListComponent) tables:TableListComponent;
  @ViewChild(ProgressBarComponent) progressBar:ProgressBarComponent;
  @ViewChild(BenchmarkResultComponent) bechmarkResults:BenchmarkResultComponent;


  constructor(private http:Http) {
  }

  ngOnInit() {
  }


  optimize() {

    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});
    let body =
    {
      tables: [],
      queries: []
    };


    body.tables = this.tables.getTables();
    body.queries = this.queries.getQueries();
    this.http.post('/api/optimize', body, options).subscribe(data => {
      console.log('Returned: ' + data);
      this.status = "IN_PROGRESS";
      this.process = data.json()['processId'];
      this.intervalId = setInterval(() => {
        let params = {
          processId: this.process
        };
        this.http.get('/api/status', {params: params}).subscribe(data => {
          this.processStatusResponse(data.json());
        });
      }, 1000);
    });
  }


  private processStatusResponse(data) {
    if (data['status'] == 'FINISHED') {
      this.status = 'FINISHED';
      this.substatuses = data['substatuses'];
      this.results = data['benchmarkResults'];
      console.log(this.results);
      this.bechmarkResults.setResults(this.results);
      clearInterval(this.intervalId);
    } else if (data['status'] == "ERROR") {
      this.status = 'ERROR';
      clearInterval(this.intervalId);
    } else if (data['status'] == "NEW"
      || data['status'] == "PARSING_INPUT"
      || data['status'] == "INPUT_PARSED"
      || data['status'] == "GENERATING_PROJECTIONS"
      || data['status'] == "PROJECTIONS_GENERATED"
      || data['status'] == "MERGING_PROJECTIONS"
      || data['status'] == "PROJECTIONS_MERGED"
      || data['status'] == "PRIORITIZING_PROJECTIONS"
      || data['status'] == "PROJECTIONS_PRIORITIZED"
      || data['status'] == "BENCHMARKING"
    ) {
      this.status = "IN_PROGRESS";
    }
    this.substatuses = data['substatuses'];
  }

  isResultReady() {
    return this.status == "FINISHED";
  }

  getCurrentProcessStatus() {
    return this.status;
  }

  isNew() {
    return this.status == "NEW";
  }

  isInProgress() {
    return this.status == "IN_PROGRESS";
  }

  resetProcess() {
    this.process = null;
    this.status = "NEW";
    this.results=[];
    this.bechmarkResults.resetResults();
    clearInterval(this.intervalId);
    //  TODO: send CANCEL
  }


}
