import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../model/book';

const booksApiPrefix = '/api/books';

@Injectable({
  providedIn: 'root'
})
export class BooksService {

  constructor(private readonly http: HttpClient) { }

  getAllBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(booksApiPrefix);
  }

  // 1a Metoda do pobierania szczegółów konkretnej książki
  findBookById(id: number | string): Observable<Book> {
    return this.http.get<Book>(`${booksApiPrefix}/${id}`);
  }

  // 2d Metoda do zapisywania zmodyfikowanej książki
  saveBook(id: number | string, book: Book): Observable<Book> {
    return this.http.put<Book>(`${booksApiPrefix}/${id}`, book);
  }

  // 5 Metoda do wyszukiwania pełnotekstowego
  searchBooks(query: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${booksApiPrefix}?q=${query}`);
  }
}
