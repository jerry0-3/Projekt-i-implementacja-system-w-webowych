import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { afterEach, beforeEach, describe, expect, it } from 'vitest';

import { RepertoireService } from './repertoire';
import {
  MovieDictionaryDto,
  PagedRepertoireResponse,
  ScreeningSeatView
} from '../model/repertoire.model';

describe('RepertoireService', () => {
  let service: RepertoireService;
  let httpMock: HttpTestingController;

  const BASE_API_URL = '/api/queries';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [RepertoireService]
    });

    service = TestBed.inject(RepertoireService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    TestBed.resetTestingModule();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getMovies', () => {
    it('should fetch movies dictionary', () => {
      const mockResponse: MovieDictionaryDto[] = [
        { id: 1, title: 'Movie A' } as MovieDictionaryDto,
        { id: 2, title: 'Movie B' } as MovieDictionaryDto
      ];

      service.getMovies().subscribe(res => {
        expect(res).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(
        '/api/queries/dictionaries/movies'
      );

      expect(req.request.method).toBe('GET');

      req.flush(mockResponse);
    });
  });

  describe('getRooms', () => {
    it('should fetch rooms list', () => {
      const mockResponse = ['Room A', 'Room B'];

      service.getRooms().subscribe(res => {
        expect(res).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(
        '/api/queries/dictionaries/rooms'
      );

      expect(req.request.method).toBe('GET');

      req.flush(mockResponse);
    });
  });

  describe('getRepertoire', () => {
    it('should fetch repertoire with default params', () => {
      const mockResponse = {} as PagedRepertoireResponse;

      service.getRepertoire().subscribe(res => {
        expect(res).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(request => {
        return (
          request.url === '/api/queries/repertoire' &&
          request.params.get('page') === '0' &&
          request.params.get('size') === '10' &&
          request.params.get('sort') === 'startTime,asc'
        );
      });

      expect(req.request.method).toBe('GET');

      req.flush(mockResponse);
    });

    it('should apply custom pagination params', () => {
      const mockResponse = {} as PagedRepertoireResponse;

      service.getRepertoire(2, 20).subscribe(res => {
        expect(res).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(request => {
        return (
          request.params.get('page') === '2' &&
          request.params.get('size') === '20'
        );
      });

      expect(req.request.method).toBe('GET');

      req.flush(mockResponse);
    });

    it('should include movieId and roomName filters', () => {
      const mockResponse = {} as PagedRepertoireResponse;

      service
        .getRepertoire(0, 10, 5, 'Room A')
        .subscribe(res => {
          expect(res).toEqual(mockResponse);
        });

      const req = httpMock.expectOne(request => {
        return (
          request.params.get('movieId') === '5' &&
          request.params.get('roomName') === 'Room A'
        );
      });

      expect(req.request.method).toBe('GET');

      req.flush(mockResponse);
    });

    it('should not include optional params when null/undefined', () => {
      const mockResponse = {} as PagedRepertoireResponse;

      service
        .getRepertoire(0, 10, null, null)
        .subscribe(res => {
          expect(res).toEqual(mockResponse);
        });

      const req = httpMock.expectOne(request => {
        return (
          !request.params.has('movieId') &&
          !request.params.has('roomName')
        );
      });

      expect(req.request.method).toBe('GET');

      req.flush(mockResponse);
    });
  });

  describe('getSeatsForScreening', () => {
    it('should fetch seats for screening', () => {
      const screeningId = 123;

      const mockResponse: ScreeningSeatView[] = [
        {
          id: 1,
          screeningId: 123,
          originalSeatId: 1,
          seatNumber: 1,
          rowNumber: 1,
          available: true
        } as ScreeningSeatView,
        {
          id: 2,
          screeningId: 123,
          originalSeatId: 2,
          seatNumber: 2,
          rowNumber: 1,
          available: true
        } as ScreeningSeatView
      ];

      service
        .getSeatsForScreening(screeningId)
        .subscribe(res => {
          expect(res).toEqual(mockResponse);
        });

      const req = httpMock.expectOne(
        `${BASE_API_URL}/screenings/${screeningId}/seats`
      );

      expect(req.request.method).toBe('GET');

      req.flush(mockResponse);
    });

    it('should handle empty seats response', () => {
      const screeningId = 999;

      const mockResponse: ScreeningSeatView[] = [];

      service
        .getSeatsForScreening(screeningId)
        .subscribe(res => {
          expect(res.length).toBe(0);
        });

      const req = httpMock.expectOne(
        `${BASE_API_URL}/screenings/${screeningId}/seats`
      );

      expect(req.request.method).toBe('GET');

      req.flush(mockResponse);
    });
  });
});