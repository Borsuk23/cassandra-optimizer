import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {TableComponent} from "../table/table.component";

@Component({
  selector: 'app-table-list',
  templateUrl: './table-list.component.html',
  styleUrls: ['./table-list.component.css']
})
export class TableListComponent implements OnInit {

  tableNumbers = [];
  @ViewChildren(TableComponent) tables: QueryList<TableComponent>;

  constructor() {
  }

  ngOnInit() {
  }

  addTable() {
    this.tableNumbers.push("test");
  }

  getTables() {
    let results = [];
    this.tables.forEach(item => {
      results.push(item.getTable())
    });
    return results;
  }

  reset(){
    this.tableNumbers=[];
  }
}
