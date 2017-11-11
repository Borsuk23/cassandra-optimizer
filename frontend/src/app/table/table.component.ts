import {Component, OnInit} from '@angular/core';
import {MatCardModule} from '@angular/material';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit {

  dataDistributionDictionary = [
    {value: 'GAUSSIAN', viewValue: 'Gauss'},
    {value: 'UNIFORM', viewValue: 'Równomierne'},
    {value: 'FIXED', viewValue: 'Stałe'},
    {value: 'EXTREME', viewValue: 'Skrajne'},
    {value: 'EXP', viewValue: 'Wykładnicze'}
  ];
  tableText = '';
  recordsNo = 0;
  dataDistribution = "NORMAL";

  constructor() {
  }

  ngOnInit() {
  }

  getTable() {
    return {
      table: this.tableText,
      recordsNo: this.recordsNo,
      dataDistribution: this.dataDistribution
    }
  }

}
