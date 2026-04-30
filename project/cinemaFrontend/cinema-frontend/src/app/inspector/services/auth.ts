import { Injectable, signal, inject } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Nowoczesne wstrzykiwanie omijające potencjalne zapętlenia w konstruktorze
  private router = inject(Router);

  // Inicjalizacja sygnału z bezpiecznym sprawdzeniem
  isLoggedIn = signal<boolean>(this.hasToken());

  login(username: string, pass: string): void {
    if (typeof window !== 'undefined') {
      const token = btoa(`${username}:${pass}`);
      localStorage.setItem('inspector_token', token);
    }
    this.isLoggedIn.set(true);
    this.router.navigate(['/inspector/scanner']);
  }

  logout(): void {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('inspector_token');
    }
    this.isLoggedIn.set(false);
    this.router.navigate(['/inspector/login']);
  }

  getToken(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('inspector_token');
    }
    return null;
  }

  private hasToken(): boolean {
    if (typeof window !== 'undefined') {
      return !!localStorage.getItem('inspector_token');
    }
    return false;
  }
}