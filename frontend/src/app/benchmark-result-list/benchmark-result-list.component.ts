import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-benchmark-result-list',
  templateUrl: './benchmark-result-list.component.html',
  styleUrls: ['./benchmark-result-list.component.css']
})
export class BenchmarkResultListComponent implements OnInit {

  benchmarkResults = [];


  constructor() {
  }

  ngOnInit() {
  }

  setResults(data) {
    data.forEach(item => this.benchmarkResults.push(JSON.stringify(item)));
  }

  reset(){
    this.benchmarkResults=[];
  }

}
