import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import {
  describe,
  it,
  expect,
  beforeEach,
  afterEach
} from 'vitest';

import { TicketCommandService } from './ticket-command';
import {
  SeatInfoDto,
  TicketDetailsResponse
} from '../model/inspector.model';

describe('TicketCommandService', () => {
  let service: TicketCommandService;
  let httpMock: HttpTestingController;

  const API_URL = '/api/commands/tickets';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TicketCommandService]
    });

    service = TestBed.inject(TicketCommandService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    TestBed.resetTestingModule();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('scanTicket', () => {
    it('should send GET request and return ticket details', () => {
      const uniqueCode = 'ABC123';

      const mockResponse: TicketDetailsResponse = {
        status: 'WAŻNY',
        totalSeats: 1,
        roomName: 'Sala 6 - 3D',
        movieTitle: 'Diuna: Część Druga',
        seats: [
          {
            seatNumber: 1,
            rowNumber: 1
          } as SeatInfoDto
        ]
      } as TicketDetailsResponse;

      service.scanTicket(uniqueCode).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(
        `${API_URL}/${uniqueCode}`
      );

      expect(req.request.method).toBe('GET');

      req.flush(mockResponse);
    });

    it('should handle scanTicket error response', () => {
      const uniqueCode = 'INVALID_CODE';

      service.scanTicket(uniqueCode).subscribe({
        next: () => {
          throw new Error('Expected request to fail');
        },
        error: (error) => {
          expect(error.status).toBe(404);
        }
      });

      const req = httpMock.expectOne(
        `${API_URL}/${uniqueCode}`
      );

      expect(req.request.method).toBe('GET');

      req.flush('Ticket not found', {
        status: 404,
        statusText: 'Not Found'
      });
    });
  });

  describe('cancelTicket', () => {
    it('should send POST request to consume ticket', () => {
      const uniqueCode = 'ABC123';

      service.cancelTicket(uniqueCode).subscribe((response) => {
        expect(response).toBeNull();
      });

      const req = httpMock.expectOne(
        `${API_URL}/${uniqueCode}/consume`
      );

      expect(req.request.method).toBe('POST');

      expect(req.request.body).toEqual({});

      req.flush(null);
    });

    it('should handle cancelTicket error response', () => {
      const uniqueCode = 'ABC123';

      service.cancelTicket(uniqueCode).subscribe({
        next: () => {
          throw new Error('Expected request to fail');
        },
        error: (error) => {
          expect(error.status).toBe(400);
        }
      });

      const req = httpMock.expectOne(
        `${API_URL}/${uniqueCode}/consume`
      );

      expect(req.request.method).toBe('POST');

      req.flush('Ticket already consumed', {
        status: 400,
        statusText: 'Bad Request'
      });
    });
  });
});