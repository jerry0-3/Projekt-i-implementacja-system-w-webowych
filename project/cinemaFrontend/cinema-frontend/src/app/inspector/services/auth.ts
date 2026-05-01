import { Injectable, signal, inject } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Nowoczesne wstrzykiwanie omijające potencjalne zapętlenia w konstruktorze
  private router = inject(Router);
  private http = inject(HttpClient);

  // Inicjalizacja sygnału z bezpiecznym sprawdzeniem
  isLoggedIn = signal<boolean>(this.hasToken());

  // Metoda zwraca Observable, by komponent logowania wiedział o wyniku
  login(username: string, pass: string): Observable<any> {
    const token = btoa(`${username}:${pass}`);
    
    // Tworzymy nagłówek ręcznie tylko na potrzeby tego jednego, testowego zapytania
    const headers = new HttpHeaders({ Authorization: `Basic ${token}` });

    return this.http.get('/api/commands/tickets/verify', { headers, responseType: 'text' }).pipe(
      tap(() => {
        // Ten kod wykona się tylko jeśli backend przepuści zapytanie (hasło jest dobre)
        if (typeof window !== 'undefined') {
          localStorage.setItem('inspector_token', token);
        }
        this.isLoggedIn.set(true);
        this.router.navigate(['/inspector/scanner']);
      })
    );
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