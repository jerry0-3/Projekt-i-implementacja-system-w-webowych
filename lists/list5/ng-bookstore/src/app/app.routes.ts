import { Routes } from '@angular/router';
import {BookListComponent} from "./books/components/book-list/book-list.component";
import {bookListResolver} from "./books/resolvers/book-list.resolver";
import { BookDetailsComponent } from "./books/components/book-details/book-details.component";
import { bookDetailsResolver } from "./books/resolvers/book-details.resolver";
import { BookEditComponent } from './books/components/book-edit/book-edit.component';
import { reviewsResolver } from "./books/resolvers/reviews.resolver";

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
  },
  {
    // 1c Dodanie nowego routingu z parametrem :bookId obsługiwanym przez resolver
    path: 'books/:bookId/reviews',
    component: BookDetailsComponent,
    resolve: {
      book: bookDetailsResolver,
      reviews: reviewsResolver // 3c Dodajemy resolver recenzji
    }
  },
  {
    // 2a Routing dla edycji
    path: 'books/:bookId/edit',
    component: BookEditComponent,
    resolve: {
      book: bookDetailsResolver // Używamy istniejącego resolvera!
    }
  },
];
