import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BenchmarkResultListComponent } from './benchmark-result-list.component';

describe('BenchmarkResultListComponent', () => {
  let component: BenchmarkResultListComponent;
  let fixture: ComponentFixture<BenchmarkResultListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BenchmarkResultListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BenchmarkResultListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
