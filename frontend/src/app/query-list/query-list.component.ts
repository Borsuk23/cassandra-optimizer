import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {QueryComponent} from '../query/query.component';

@Component({
  selector: 'app-query-list',
  templateUrl: './query-list.component.html',
  styleUrls: ['./query-list.component.css']
})
export class QueryListComponent implements OnInit {

  queryNumbers = [];
  @ViewChildren(QueryComponent) queries: QueryList<QueryComponent>;

  constructor() {
  }

  ngOnInit() {
  }

  addQuery() {
    this.queryNumbers.push("test");
  }

  getQueries() {
    let results = [];
    this.queries.forEach(item => {
      results.push(item.getQuery())
    });
    return results;
  }
}
