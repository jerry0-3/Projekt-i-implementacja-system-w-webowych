import { TestBed } from '@angular/core/testing';
import {
  HttpClient,
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withInterceptors
} from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting
} from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';

import { AuthService } from './auth';
import { authInterceptor } from './auth.interceptor';

describe('AuthService + authInterceptor', () => {
  let service: AuthService;
  let http: HttpClient;
  let httpMock: HttpTestingController;

  let router: {
    navigate: ReturnType<typeof vi.fn>;
  };

  beforeEach(() => {
    router = {
      navigate: vi.fn()
    };

    TestBed.configureTestingModule({
      providers: [
        AuthService,

        // Router mock
        { provide: Router, useValue: router },

        // Modern HttpClient setup (REQUIRED for interceptor + testing)
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(AuthService);
    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);

    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
    vi.clearAllMocks();
  });

  it('should create service', () => {
    expect(service).toBeTruthy();
  });

  // -----------------------------
  // AUTH SERVICE LOGIN
  // -----------------------------
  describe('login()', () => {
    it('should send request with Basic auth + X-Requested-With headers', () => {
      service.login('john', 'secret').subscribe();

      const req = httpMock.expectOne('/api/commands/tickets/verify');

      const token = btoa('john:secret');

      expect(req.request.headers.get('Authorization')).toBe(
        `Basic ${token}`
      );

      expect(req.request.headers.get('X-Requested-With')).toBe(
        'XMLHttpRequest'
      );

      req.flush('ok');
    });

    it('should store token, update signal and navigate on success', () => {
      service.login('john', 'secret').subscribe();

      const req = httpMock.expectOne('/api/commands/tickets/verify');

      req.flush('ok');

      const token = btoa('john:secret');

      expect(localStorage.getItem('inspector_token')).toBe(token);
      expect(service.isLoggedIn()).toBe(true);

      expect(router.navigate).toHaveBeenCalledWith([
        '/inspector/scanner'
      ]);
    });
  });

  // -----------------------------
  // INTERCEPTOR BEHAVIOR
  // -----------------------------
  describe('authInterceptor', () => {
    it('should attach token from localStorage to API requests', () => {
      const token = 'my-token';
      localStorage.setItem('inspector_token', token);

      http.get('/api/commands/test').subscribe();

      const req = httpMock.expectOne('/api/commands/test');

      expect(req.request.headers.get('Authorization')).toBe(
        `Basic ${token}`
      );

      req.flush({});
    });

    it('should NOT attach token for non-command endpoints', () => {
      localStorage.setItem('inspector_token', 'token');

      http.get('/api/public/data').subscribe();

      const req = httpMock.expectOne('/api/public/data');

      expect(req.request.headers.has('Authorization')).toBe(false);

      req.flush({});
    });

    it('should logout user on 401 error (non-verify endpoint)', () => {
      localStorage.setItem('inspector_token', 'token');

      const alertSpy = vi
        .spyOn(window, 'alert')
        .mockImplementation(() => {});

      http.get('/api/commands/tickets/some-endpoint').subscribe({
        error: () => {}
      });

      const req = httpMock.expectOne(
        '/api/commands/tickets/some-endpoint'
      );

      req.flush('Unauthorized', {
        status: 401,
        statusText: 'Unauthorized'
      });

      expect(router.navigate).toHaveBeenCalledWith([
        '/inspector/login'
      ]);

      expect(localStorage.getItem('inspector_token')).toBeNull();
      expect(alertSpy).toHaveBeenCalled();
    });

    it('should NOT logout on 401 from /verify endpoint', () => {
      const alertSpy = vi
        .spyOn(window, 'alert')
        .mockImplementation(() => {});

      http.get('/api/commands/tickets/verify').subscribe({
        error: () => {}
      });

      const req = httpMock.expectOne('/api/commands/tickets/verify');

      req.flush('Unauthorized', {
        status: 401,
        statusText: 'Unauthorized'
      });

      expect(router.navigate).not.toHaveBeenCalled();
      expect(alertSpy).not.toHaveBeenCalled();
    });
  });

  // -----------------------------
  // LOGOUT
  // -----------------------------
  describe('logout()', () => {
    it('should clear token and navigate to login', () => {
      localStorage.setItem('inspector_token', 'token');

      service.logout();

      expect(localStorage.getItem('inspector_token')).toBeNull();
      expect(service.isLoggedIn()).toBe(false);

      expect(router.navigate).toHaveBeenCalledWith([
        '/inspector/login'
      ]);
    });
  });

  // -----------------------------
  // GET TOKEN
  // -----------------------------
  describe('getToken()', () => {
    it('should return token from localStorage', () => {
      localStorage.setItem('inspector_token', 'abc');

      expect(service.getToken()).toBe('abc');
    });

    it('should return null if no token', () => {
      expect(service.getToken()).toBeNull();
    });
  });
});