import {Component, OnInit} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Headers, RequestOptions} from '@angular/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

@Component({
  selector: 'app-optimize-form',
  templateUrl: './optimize-form.component.html',
  styleUrls: ['./optimize-form.component.css']
})
export class OptimizeFormComponent implements OnInit {
  status: String = "NEW";
  process: String = "test";

  constructor(private http: Http) {
  }

  ngOnInit() {
  }

  optimize() {
    console.log('tutaj cos wyslac trzeba nie?');

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
    this.http.post("/api/optimize", body, options)
      .map(this.extractData)
      .catch(this.handleErrorObservable);
    this.status = "IN_PROGRESS";
  }

  isResultReady() {
    return this.status == "DONE";
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
  }

  private extractData(res: Response) {
    let body = res.json();
    return body.data || {};
  }

  private handleErrorObservable (error: Response | any) {
    console.error(error.message || error);
    return Observable.throw(error.message || error);
  }

}
