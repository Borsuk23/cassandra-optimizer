import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgressStepComponent } from './progress-step.component';

describe('ProgressStepComponent', () => {
  let component: ProgressStepComponent;
  let fixture: ComponentFixture<ProgressStepComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProgressStepComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProgressStepComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
