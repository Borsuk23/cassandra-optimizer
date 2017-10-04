import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-query',
  templateUrl: './query.component.html',
  styleUrls: ['./query.component.css']
})
export class QueryComponent implements OnInit {

  queryText = '';
  queryFrequency = 0;
  executionTime = 0;
  importance = 0;

  constructor() {
  }

  ngOnInit() {
  }

  getQuery() {
    return {
      query: this.queryText,
      frequency: this.queryFrequency,
      executionTime: this.executionTime,
      importance: this.importance
    }
  }

}
