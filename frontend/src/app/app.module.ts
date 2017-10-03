import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MdCardModule} from '@angular/material';
import {MdInputModule} from '@angular/material';

import { AppComponent } from './app.component';
import { QueryComponent } from './query/query.component';
import { BenchmarkResultComponent } from './benchmark-result/benchmark-result.component';
import { OptimizeFormComponent } from './optimize-form/optimize-form.component';
import { TableListComponent } from './table-list/table-list.component';
import { QueryListComponent } from './query-list/query-list.component';
import { TableComponent } from './table/table.component';
import { ProgressBarComponent } from './progress-bar/progress-bar.component';

@NgModule({
  declarations: [
    AppComponent,
    QueryComponent,
    BenchmarkResultComponent,
    OptimizeFormComponent,
    TableListComponent,
    QueryListComponent,
    TableComponent,
    ProgressBarComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    BrowserAnimationsModule,
    MdCardModule,
    MdInputModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
