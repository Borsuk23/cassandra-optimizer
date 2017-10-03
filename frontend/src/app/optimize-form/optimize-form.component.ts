import {Component, OnInit} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

@Component({
  selector: 'app-optimize-form',
  templateUrl: './optimize-form.component.html',
  styleUrls: ['./optimize-form.component.css']
})
export class OptimizeFormComponent implements OnInit {
  status: String = "NEW";
  substatuses: String[] = [];
  process: String = "test";
  intervalId;

  constructor(private http: Http) {
  }

  ngOnInit() {
  }


  optimize() {

    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});
    let body = `
    {
	"tables": [{
			"table": "CREATE TABLE test (groupname string, column2 numeric, column3 date)",
			"recordsNo": 1000,
			"dataDistribution": "NORMAL"
		},
		{
			"table": "CREATE TABLE group_join_dates(groupname string, column22 numeric, column33 date)",
			"recordsNo": 1000,
			"dataDistribution": "NORMAL"
		}
	],
	"queries": [{
			"query": "SELECT * FROM group_join_dates WHERE groupname = ? ORDER BY joined DESC LIMIT 10;",
			"frequency": 1000,
			"executionTime": 100,
			"importance": 10
		},
		{
			"query": "SELECT * FROM test WHERE column3>=1 ORDER BY groupname DESC and column33 ASC",
			"frequency": 1000,
			"executionTime": 100,
			"importance": 10

		}
	]
}`;
    this.status = "IN_PROGRESS";


    this.http.post('/api/optimize', body, options).subscribe(data => {
      console.log('Returned: ' + data);
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
      this.substatuses.push('FINISHED');
      clearInterval(this.intervalId);
    } else if (data['status'] == "ERROR") {
      this.status = 'ERROR';
      clearInterval(this.intervalId);
    } else if (data['status'] == "NEW") {
      this.status = "IN_PROGRESS";
      this.substatuses.push(data['status']);
    } else if (data['status'] == "PARSING_INPUT") {
      this.status = "IN_PROGRESS";
      this.substatuses.push(data['status']);
    } else if (data['status'] == "INPUT_PARSED") {
      this.status = "IN_PROGRESS";
      this.substatuses.push(data['status']);
    } else if (data['status'] == "GENERATING_PROJECTIONS") {
      this.status = "IN_PROGRESS";
      this.substatuses.push(data['status']);
    } else if (data['status'] == "PROJECTIONS_GENERATED") {
      this.status = "IN_PROGRESS";
      this.substatuses.push(data['status']);
    } else if (data['status'] == "MERGING_PROJECTIONS") {
      this.status = "IN_PROGRESS";
      this.substatuses.push(data['status']);
    } else if (data['status'] == "PROJECTIONS_MERGED") {
      this.status = "IN_PROGRESS";
      this.substatuses.push(data['status']);
    } else if (data['status'] == "PRIORITIZING_PROJECTIONS") {
      this.status = "IN_PROGRESS";
      this.substatuses.push(data['status']);
    } else if (data['status'] == "PROJECTIONS_PRIORITIZED") {
      this.status = "IN_PROGRESS";
      this.substatuses.push(data['status']);
    } else if (data['status'] == "BENCHMARKING") {
      this.status = "IN_PROGRESS";
      this.substatuses.push(data['status']);
    }
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
    clearInterval(this.intervalId);
    //  TODO: send CANCEL
  }


}
