import {Component, OnInit, QueryList, ViewChild} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import {QueryListComponent} from "../query-list/query-list.component";
import {TableListComponent} from "../table-list/table-list.component";
import {ProgressBarComponent} from "../progress-bar/progress-bar.component";
import {BenchmarkResultListComponent} from "../benchmark-result-list/benchmark-result-list.component";

@Component({
  selector: 'app-optimize-form',
  templateUrl: './optimize-form.component.html',
  styleUrls: ['./optimize-form.component.css']
})
export class OptimizeFormComponent implements OnInit {
  status: String = "NEW";
  errorMessage: String;
  substatuses: String[] = [];
  process: String = "test";
  intervalId;
  results = [];
  @ViewChild(QueryListComponent) queries: QueryListComponent;
  @ViewChild(TableListComponent) tables: TableListComponent;
  @ViewChild(ProgressBarComponent) progressBar: ProgressBarComponent;
  @ViewChild(BenchmarkResultListComponent) bechmarkResults: BenchmarkResultListComponent;


  constructor(private http: Http) {
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
      this.results = data['benchmarkResults'];
      this.bechmarkResults.setResults(this.results);
      clearInterval(this.intervalId);
    } else if (data['status'] == "ERROR") {
      this.status = 'ERROR';
      this.errorMessage = data['errorMessage'];
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
    this.progressBar.updateStatuses(this.substatuses, this.status, this.errorMessage);
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
    return this.status == "IN_PROGRESS" || this.status == "ERROR";
  }

  sendCancel(processId) {
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});
    let body =
      {
        processId: processId
      };

    this.http.post('/api/cancel', body, options);
  }

  resetProcess() {
    this.sendCancel(this.process);
    this.process = null;
    this.status = "NEW";
    this.results = [];
    this.substatuses = [];
    this.errorMessage = null;
    this.bechmarkResults.reset();
    this.progressBar.reset();
    this.tables.reset();
    this.queries.reset();
    clearInterval(this.intervalId);
    this.intervalId = null;
  }


}
