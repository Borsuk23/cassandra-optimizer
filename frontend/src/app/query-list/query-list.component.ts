import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {MdInputModule} from '@angular/material';
import {QueryComponent} from '../query/query.component';

@Component({
  selector: 'app-query-list',
  templateUrl: './query-list.component.html',
  styleUrls: ['./query-list.component.css']
})
export class QueryListComponent implements OnInit {
  @ViewChildren(QueryComponent) queries: QueryList<QueryComponent>

  constructor() {
  }

  ngOnInit() {
  }

  addQuery() {
    console.log('test');
    this.queries.toArray().push(new QueryComponent())
    console.log(this.queries.length);
  }
}
