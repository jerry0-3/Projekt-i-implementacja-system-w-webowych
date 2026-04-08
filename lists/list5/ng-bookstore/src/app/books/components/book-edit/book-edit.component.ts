import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Book } from '../../model/book';
import { BooksService } from '../../services/books.service';

@Component({
  selector: 'bs-book-edit',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink], // Importujemy moduł formularzy
  templateUrl: './book-edit.component.html',
  styleUrls: ['./book-edit.component.scss']
})
export class BookEditComponent implements OnInit {
  bookForm!: FormGroup;
  bookId!: number;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly formBuilder: FormBuilder,
    private readonly booksService: BooksService
  ) {}

  ngOnInit(): void {
    // Pobieramy dane z resolvera
    const book: Book = this.activatedRoute.snapshot.data['book'];
    this.bookId = book.id;

    // Inicjalizacja formularza z regułami walidacji 2c
    this.bookForm = this.formBuilder.group({
      title: [book.title, [Validators.required, Validators.maxLength(50)]],
      author: [book.author, [Validators.required, Validators.maxLength(50)]],
      year: [book.year, [Validators.required, Validators.min(1000), Validators.max(2023)]],
      description: [book.description, [Validators.maxLength(1000)]]
    });
  }

  // 2e Obsługa zapisu
  onSave(): void {
    if (this.bookForm.valid) {
      // Łączymy ID (którego nie ma w formularzu) z danymi z formularza
      const updatedBook: Book = {
        id: this.bookId,
        ...this.bookForm.value
      };

      this.booksService.saveBook(this.bookId, updatedBook).subscribe(() => {
        // Przekierowanie po pomyślnym zapisie
        this.router.navigate(['/books']);
      });
    }
  }

  // 2f Obsługa anulowania
  onCancel(): void {
    this.router.navigate(['/books']);
  }
}