import { TestBed } from '@angular/core/testing';
import {
  Router,
  UrlTree,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
} from '@angular/router';

import { describe, it, expect, beforeEach, vi } from 'vitest';

import { authGuard } from './auth-guard';
import { AuthService } from '../services/auth';

describe('authGuard', () => {
  let mockAuthService: {
    isLoggedIn: ReturnType<typeof vi.fn>;
  };

  let mockRouter: {
    createUrlTree: ReturnType<typeof vi.fn>;
  };

  let route: ActivatedRouteSnapshot;
  let state: RouterStateSnapshot;

  beforeEach(() => {
    mockAuthService = {
      isLoggedIn: vi.fn(),
    };

    mockRouter = {
      createUrlTree: vi.fn(),
    };

    route = {} as ActivatedRouteSnapshot;

    state = {
      url: '/inspector/scanner',
    } as RouterStateSnapshot;

    TestBed.configureTestingModule({
      providers: [
        {
          provide: AuthService,
          useValue: mockAuthService,
        },
        {
          provide: Router,
          useValue: mockRouter,
        },
      ],
    });
  });

  it('should allow access when user is logged in', () => {
    mockAuthService.isLoggedIn.mockReturnValue(true);

    const result = TestBed.runInInjectionContext(() =>
      authGuard(route, state)
    );

    expect(result).toBe(true);
    expect(mockAuthService.isLoggedIn).toHaveBeenCalledOnce();
    expect(mockRouter.createUrlTree).not.toHaveBeenCalled();
  });

  it('should redirect to login when user is not logged in', () => {
    const mockUrlTree = {} as UrlTree;

    mockAuthService.isLoggedIn.mockReturnValue(false);

    mockRouter.createUrlTree.mockReturnValue(mockUrlTree);

    const result = TestBed.runInInjectionContext(() =>
      authGuard(route, state)
    );

    expect(mockAuthService.isLoggedIn).toHaveBeenCalledOnce();

    expect(mockRouter.createUrlTree).toHaveBeenCalledWith([
      '/inspector/login',
    ]);

    expect(result).toBe(mockUrlTree);
  });

  it('should call createUrlTree exactly once when unauthorized', () => {
    const mockUrlTree = {} as UrlTree;

    mockAuthService.isLoggedIn.mockReturnValue(false);

    mockRouter.createUrlTree.mockReturnValue(mockUrlTree);

    TestBed.runInInjectionContext(() =>
      authGuard(route, state)
    );

    expect(mockRouter.createUrlTree).toHaveBeenCalledTimes(1);
  });

  it('should not redirect when authorized', () => {
    mockAuthService.isLoggedIn.mockReturnValue(true);

    TestBed.runInInjectionContext(() =>
      authGuard(route, state)
    );

    expect(mockRouter.createUrlTree).not.toHaveBeenCalled();
  });
});