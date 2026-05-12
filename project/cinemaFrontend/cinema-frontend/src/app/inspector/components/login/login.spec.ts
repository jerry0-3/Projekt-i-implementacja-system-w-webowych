import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';

import { LoginComponent } from './login';
import { AuthService } from '../../services/auth';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let authService: {
    login: ReturnType<typeof vi.fn>;
    isLoggedIn: ReturnType<typeof vi.fn>;
  };

  let router: {
    navigate: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    authService = {
      login: vi.fn(),
      isLoggedIn: vi.fn()
    };

    router = {
      navigate: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        {
          provide: AuthService,
          useValue: authService
        },
        {
          provide: Router,
          useValue: router
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    fixture.destroy();
    vi.clearAllMocks();
    TestBed.resetTestingModule();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should redirect to scanner when already logged in', () => {
      authService.isLoggedIn.mockReturnValue(true);

      component.ngOnInit();

      expect(router.navigate).toHaveBeenCalledWith([
        '/inspector/scanner'
      ]);
    });

    it('should not redirect when not logged in', () => {
      authService.isLoggedIn.mockReturnValue(false);

      component.ngOnInit();

      expect(router.navigate).not.toHaveBeenCalled();
    });
  });

  describe('onSubmit', () => {
    it('should show error when username is empty', () => {
      component.username = '';
      component.password = 'password';

      component.onSubmit();

      expect(component.errorMessage).toBe(
        'Proszę podać login i hasło.'
      );

      expect(authService.login).not.toHaveBeenCalled();
    });

    it('should show error when password is empty', () => {
      component.username = 'admin';
      component.password = '';

      component.onSubmit();

      expect(component.errorMessage).toBe(
        'Proszę podać login i hasło.'
      );

      expect(authService.login).not.toHaveBeenCalled();
    });

    it('should trim username and password validation', () => {
      component.username = '   ';
      component.password = '   ';

      component.onSubmit();

      expect(component.errorMessage).toBe(
        'Proszę podać login i hasło.'
      );

      expect(authService.login).not.toHaveBeenCalled();
    });

    it('should call authService.login with username and password', () => {
      authService.login.mockReturnValue(of({}));

      component.username = 'admin';
      component.password = 'secret';

      component.onSubmit();

      expect(authService.login).toHaveBeenCalledWith(
        'admin',
        'secret'
      );
    });

    it('should not set error message on successful login', () => {
      authService.login.mockReturnValue(of({}));

      component.username = 'admin';
      component.password = 'secret';

      component.onSubmit();

      expect(component.errorMessage).toBe('');
    });

    it('should show invalid credentials error on 401 response', () => {
      authService.login.mockReturnValue(
        throwError(() => ({
          status: 401
        }))
      );

      component.username = 'admin';
      component.password = 'wrong-password';

      component.onSubmit();

      expect(component.errorMessage).toBe(
        'Nieprawidłowy login lub hasło.'
      );
    });

    it('should show server connection error on non-401 response', () => {
      authService.login.mockReturnValue(
        throwError(() => ({
          status: 500
        }))
      );

      component.username = 'admin';
      component.password = 'secret';

      component.onSubmit();

      expect(component.errorMessage).toBe(
        'Brak połączenia z serwerem.'
      );
    });

    it('should show server connection error on network error', () => {
      authService.login.mockReturnValue(
        throwError(() => ({
          status: 0
        }))
      );

      component.username = 'admin';
      component.password = 'secret';

      component.onSubmit();

      expect(component.errorMessage).toBe(
        'Brak połączenia z serwerem.'
      );
    });
  });
});