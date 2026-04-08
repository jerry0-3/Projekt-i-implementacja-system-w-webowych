import { Component, Input } from '@angular/core';
import { Review } from '../../model/review';

@Component({
  selector: 'bs-review',
  standalone: true,
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.scss']
})
export class ReviewComponent {
  // Oczekujemy, że recenzja zostanie przekazana z komponentu nadrzędnego
  @Input({ required: true }) review!: Review;
}