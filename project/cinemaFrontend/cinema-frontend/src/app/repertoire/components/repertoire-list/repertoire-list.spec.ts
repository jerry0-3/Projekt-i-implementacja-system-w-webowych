import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepertoireList } from './repertoire-list';

describe('RepertoireList', () => {
  let component: RepertoireList;
  let fixture: ComponentFixture<RepertoireList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepertoireList],
    }).compileComponents();

    fixture = TestBed.createComponent(RepertoireList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
