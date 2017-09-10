import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OptimizeFormComponent } from './optimize-form.component';

describe('OptimizeFormComponent', () => {
  let component: OptimizeFormComponent;
  let fixture: ComponentFixture<OptimizeFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OptimizeFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OptimizeFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
