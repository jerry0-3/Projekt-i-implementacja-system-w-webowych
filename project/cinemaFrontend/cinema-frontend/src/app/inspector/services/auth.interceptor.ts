import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token && req.url.includes('/api/commands/')) {
    req = req.clone({
      setHeaders: {
        Authorization: `Basic ${token}`
      }
    });
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        authService.logout();
        
        // Zabezpieczenie przed błędem na serwerze
        if (typeof window !== 'undefined') {
          alert('Sesja wygasła lub podano błędne dane logowania.');
        }
      }
      return throwError(() => error);
    })
  );
};