import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatCardModule, MatInputModule} from '@angular/material';
import {MatSelectModule} from '@angular/material/select';
import {MatIconModule} from '@angular/material/icon';
import {AppComponent} from './app.component';
import {QueryComponent} from './query/query.component';
import {BenchmarkResultListComponent} from './benchmark-result-list/benchmark-result-list.component';
import {OptimizeFormComponent} from './optimize-form/optimize-form.component';
import {TableListComponent} from './table-list/table-list.component';
import {QueryListComponent} from './query-list/query-list.component';
import {TableComponent} from './table/table.component';
import {ProgressBarComponent} from './progress-bar/progress-bar.component';
import {ProgressStepComponent} from './progress-step/progress-step.component';
import { BenchmarkResultComponent } from './benchmark-result/benchmark-result.component';
import { ResultProjectionComponent } from './result-projection/result-projection.component';
import { ResultTableComponent } from './result-table/result-table.component';
import { ResultQueriesStatsComponent } from './result-queries-stats/result-queries-stats.component';
import { ResultStressToolGraphComponent } from './result-stress-tool-graph/result-stress-tool-graph.component';

@NgModule({
  declarations: [
    AppComponent,
    QueryComponent,
    BenchmarkResultListComponent,
    OptimizeFormComponent,
    TableListComponent,
    QueryListComponent,
    TableComponent,
    ProgressBarComponent,
    ProgressStepComponent,
    BenchmarkResultComponent,
    ResultProjectionComponent,
    ResultTableComponent,
    ResultQueriesStatsComponent,
    ResultStressToolGraphComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
