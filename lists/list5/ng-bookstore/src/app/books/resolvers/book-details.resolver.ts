import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from '@angular/router';
import { BooksService } from '../services/books.service';
import { Book } from '../model/book';

export const bookDetailsResolver: ResolveFn<Book> = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
) => {
  // Pobieramy bookId ze ścieżki i przekazujemy do serwisu
  const bookId = route.paramMap.get('bookId')!;
  return inject(BooksService).findBookById(bookId);
};