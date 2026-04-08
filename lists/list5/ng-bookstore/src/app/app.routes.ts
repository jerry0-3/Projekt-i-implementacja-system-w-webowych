import { Routes } from '@angular/router';
import {BookListComponent} from "./books/components/book-list/book-list.component";
import {bookListResolver} from "./books/resolvers/book-list.resolver";

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/books'
  },
  {
    path: 'books',
    component: BookListComponent,
    resolve: {
      books: bookListResolver
    }
  }
];
