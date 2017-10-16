import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-benchmark-result',
  templateUrl: './benchmark-result.component.html',
  styleUrls: ['./benchmark-result.component.css']
})
export class BenchmarkResultComponent implements OnInit {

  benchmarkResults = [];


  constructor() {
  }

  ngOnInit() {
  }

  setResults(data) {
    data.forEach(item => this.benchmarkResults.push(JSON.stringify(item)));
  }
  
  resetResults(){
    this.benchmarkResults=[];
  }

}
