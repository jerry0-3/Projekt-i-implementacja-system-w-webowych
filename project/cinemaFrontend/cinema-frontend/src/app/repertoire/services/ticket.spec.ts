import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  afterEach,
  beforeEach,
  describe,
  expect,
  it
} from 'vitest';

import {
  TicketService,
  BuyTicketCommand,
  BuyTicketResponse
} from './ticket';

describe('TicketService', () => {
  let service: TicketService;
  let httpMock: HttpTestingController;

  const API_URL = '/api/commands/tickets';

  beforeEach(() => {
        TestBed.configureTestingModule({
          providers: [
            provideHttpClient(),
            provideHttpClientTesting(),
            TicketService
          ]
    });

    service = TestBed.inject(TicketService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    TestBed.resetTestingModule();
  });

  it('should create', () => {
    expect(service).toBeTruthy();
  });

  describe('buyTicket', () => {
    it('should send POST request with command body', () => {
      const command: BuyTicketCommand = {
        screeningId: 1,
        seatIds: [10, 11, 12]
      };

      const mockResponse: BuyTicketResponse = {
        uniqueCode: 'ABC123XYZ',
        message: 'Ticket purchased successfully'
      };

      service.buyTicket(command).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(API_URL);

      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(command);

      req.flush(mockResponse);
    });

    it('should handle empty seatIds array', () => {
      const command: BuyTicketCommand = {
        screeningId: 5,
        seatIds: []
      };

      const mockResponse: BuyTicketResponse = {
        uniqueCode: '',
        message: 'No seats selected'
      };

      service.buyTicket(command).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(API_URL);

      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(command);

      req.flush(mockResponse);
    });

    it('should handle server error response', () => {
      const command: BuyTicketCommand = {
        screeningId: 1,
        seatIds: [1, 2]
      };

      service.buyTicket(command).subscribe({
        next: () => {
          expect.fail('Expected request to fail');
        },
        error: error => {
          expect(error.status).toBe(400);
          expect(error.statusText).toBe('Bad Request');
        }
      });

      const req = httpMock.expectOne(API_URL);

      expect(req.request.method).toBe('POST');

      req.flush('Seats are already reserved', {
        status: 400,
        statusText: 'Bad Request'
      });
    });

    it('should handle network/server unavailable error', () => {
      const command: BuyTicketCommand = {
        screeningId: 1,
        seatIds: [1]
      };

      service.buyTicket(command).subscribe({
        next: () => {
          expect.fail('Expected request to fail');
        },
        error: error => {
          expect(error.status).toBe(500);
        }
      });

      const req = httpMock.expectOne(API_URL);

      req.flush('Internal server error', {
        status: 500,
        statusText: 'Server Error'
      });
    });
  });
});