import { Component } from '@angular/core';
import { Book } from '../../model/book';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
    selector: 'bs-book-list',
    templateUrl: './book-list.component.html',
    styleUrls: ['./book-list.component.scss'],
    standalone: true,
    imports: [RouterLink]
})
export class BookListComponent {

  readonly books: Book[];

  constructor(private readonly activatedRoute: ActivatedRoute) {
    this.books = this.activatedRoute.snapshot.data['books'];
  }
}
