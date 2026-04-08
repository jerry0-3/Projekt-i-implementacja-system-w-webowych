import {inject} from '@angular/core';
import {ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot} from '@angular/router';
import {BooksService} from '../services/books.service';
import {Book} from '../model/book';

export const bookListResolver: ResolveFn<Book[]> = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
) => {
  return inject(BooksService).getAllBooks();
};
