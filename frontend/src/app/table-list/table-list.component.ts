import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-table-list',
  templateUrl: './table-list.component.html',
  styleUrls: ['./table-list.component.css']
})
export class TableListComponent implements OnInit {

  tables: String[] = [];

  constructor() {
  }

  ngOnInit() {
  }

  addTable()
  {
    this.tables.push("test");
  }
}
