import { Component } from '@angular/core';
import { Book } from '../../model/book';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormControl, ReactiveFormsModule } from '@angular/forms'; // Import formularzy
import { BooksService } from '../../services/books.service'; // Import serwisu
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs'; // Operatory RxJS

@Component({
    selector: 'bs-book-list',
    templateUrl: './book-list.component.html',
    styleUrls: ['./book-list.component.scss'],
    standalone: true,
    imports: [RouterLink, ReactiveFormsModule]
})
export class BookListComponent {

  books: Book[];
  searchControl = new FormControl('');

  constructor(private readonly activatedRoute: ActivatedRoute, private readonly booksService: BooksService) {
    this.books = this.activatedRoute.snapshot.data['books'];
  }

  ngOnInit(): void {
    // Nasłuchiwanie na zmiany wpisywane przez użytkownika w czasie rzeczywistym
    this.searchControl.valueChanges.pipe(
      // 5d Czekamy 200ms od ostatniego wciśnięcia klawisza
      debounceTime(200),
      // Uruchamiamy zapytanie tylko jeśli tekst faktycznie się zmienił
      distinctUntilChanged(),
      // Przełączamy się na nowy strumień (zapytanie HTTP) i anulujemy poprzednie jeśli jeszcze trwało
      switchMap((query) => {
        const safeQuery = query || '';
        // 5b Sprawdzamy czy wpisano co najmniej 2 znaki
        if (safeQuery.length >= 2) {
          return this.booksService.searchBooks(safeQuery);
        } else {
          // Jeśli znaków jest mniej niż 2 (lub pole jest puste), wracamy do pełnej listy
          return this.booksService.getAllBooks();
        }
      })
    ).subscribe((filteredBooks) => {
      // 5c Aktualizujemy listę książek bez przeładowywania strony
      this.books = filteredBooks;
    });
  }
}
