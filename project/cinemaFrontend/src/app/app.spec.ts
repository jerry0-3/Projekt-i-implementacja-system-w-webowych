import { provideRouter } from '@angular/router';
import { TestBed } from '@angular/core/testing';
import { describe, it, expect, beforeEach, vi } from 'vitest';

import { App } from './app';
import { AuthService } from './inspector/services/auth';
import { signal, WritableSignal } from '@angular/core';

describe('App Component', () => {
  let component: App;

  let authService: {
    isLoggedIn: WritableSignal<boolean>;
    logout: ReturnType<typeof vi.fn>;
  };

  beforeEach(async () => {
    authService = {
      isLoggedIn: signal(true),
      logout: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authService }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(App);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should have default title signal value', () => {
    expect(component['title']()).toBe('cinema-frontend');
  });

  it('should inject AuthService', () => {
    expect(component.authService).toBeDefined();
  });

  it('should call authService.logout on onLogout', () => {
    component.onLogout();

    expect(authService.logout).toHaveBeenCalledTimes(1);
  });

  it('should allow multiple logout calls', () => {
    component.onLogout();
    component.onLogout();
    component.onLogout();

    expect(authService.logout).toHaveBeenCalledTimes(3);
  });

  it('should not throw when logout is called', () => {
    authService.logout.mockImplementation(() => {});

    expect(() => component.onLogout()).not.toThrow();
  });

  it('should keep title signal stable across calls', () => {
    const first = component['title']();

    component.onLogout();
    component.onLogout();

    const second = component['title']();

    expect(first).toBe(second);
  });
});