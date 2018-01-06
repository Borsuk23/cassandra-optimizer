import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-benchmark-result',
  templateUrl: './benchmark-result.component.html',
  styleUrls: ['./benchmark-result.component.css']
})
export class BenchmarkResultComponent implements OnInit {

  @Input()
  public result: {};

  constructor() {
  }

  ngOnInit() {
  }


  getTables() {

  }

  getQueriesStats() {
  }

  getGraph() {

  }

}
