import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketSummary } from './ticket-summary';

describe('TicketSummary', () => {
  let component: TicketSummary;
  let fixture: ComponentFixture<TicketSummary>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketSummary],
    }).compileComponents();

    fixture = TestBed.createComponent(TicketSummary);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
