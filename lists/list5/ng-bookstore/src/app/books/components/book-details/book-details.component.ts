import { Component } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Book } from '../../model/book';
import { Review } from '../../model/review';
import { ReviewComponent } from '../review/review.component';
import { ReviewFormComponent } from '../review-form/review-form.component';
import { ReviewsService } from '../../services/reviews.service';

@Component({
  selector: 'bs-book-details',
  standalone: true,
  imports: [RouterLink, ReviewComponent, ReviewFormComponent], // RouterLink Wymagane dla linku powrotnego (routerLink)
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.scss']
})
export class BookDetailsComponent {
  
  readonly book: Book;
  reviews: Review[];

  constructor(private readonly activatedRoute: ActivatedRoute, private readonly reviewsService: ReviewsService) {
    // Odczytujemy zarówno książkę, jak i jej recenzje z resolverów
    this.book = this.activatedRoute.snapshot.data['book'];
    this.reviews = this.activatedRoute.snapshot.data['reviews'];
  }

  // 4d Logika zapisu i aktualizacji widoku
  handleReviewAdded(newReviewData: Review) {
    this.reviewsService.addReview(newReviewData).subscribe((savedReview) => {
      // Dodajemy zapisaną recenzję (już z ID z serwera) do listy
      this.reviews = [...this.reviews, savedReview];
    });
  }
}