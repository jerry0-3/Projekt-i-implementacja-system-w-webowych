import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepertoireListComponent } from './repertoire-list';

describe('RepertoireList', () => {
  let component: RepertoireListComponent;
  let fixture: ComponentFixture<RepertoireListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepertoireListComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RepertoireListComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
