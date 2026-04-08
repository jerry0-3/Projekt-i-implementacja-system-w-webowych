import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Review } from '../../model/review';

@Component({
  selector: 'bs-review-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './review-form.component.html'
})
export class ReviewFormComponent {
  @Input({ required: true }) bookId!: number;
  // 4d Powiadomienie rodzica o zapisaniu nowej recenzji
  @Output() reviewAdded = new EventEmitter<Review>();

  reviewForm: FormGroup;

  constructor(private fb: FormBuilder) {
    // 4b Walidacja - wszystkie pola obowiązkowe
    this.reviewForm = this.fb.group({
      author: ['', Validators.required],
      title: ['', Validators.required],
      description: ['', Validators.required],
      rate: [5, [Validators.required, Validators.min(1), Validators.max(5)]]
    });
  }

  onSubmit() {
    if (this.reviewForm.valid) {
      const newReview: Omit<Review, 'id'> = {
        ...this.reviewForm.value,
        forBook: this.bookId
      };
      // Emitujemy dane do komponentu nadrzędnego, który zajmie się zapisem przez serwis
      this.reviewAdded.emit(newReview as Review);
      this.reviewForm.reset({ rating: 5 }); // Czyszczenie formularza po wysłaniu
    }
  }
}