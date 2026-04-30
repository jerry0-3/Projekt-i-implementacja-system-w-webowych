import { TestBed } from '@angular/core/testing';

import { TicketCommand } from './ticket-command';

describe('TicketCommand', () => {
  let service: TicketCommand;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TicketCommand);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
