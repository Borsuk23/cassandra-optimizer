import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {MdInputModule} from '@angular/material';
import {QueryComponent} from '../query/query.component';

@Component({
  selector: 'app-query-list',
  templateUrl: './query-list.component.html',
  styleUrls: ['./query-list.component.css']
})
export class QueryListComponent implements OnInit {

  queries: String[] = [];
  constructor() {
  }

  ngOnInit() {
  }

  addQuery() {
    this.queries.push("test");
  }
}
