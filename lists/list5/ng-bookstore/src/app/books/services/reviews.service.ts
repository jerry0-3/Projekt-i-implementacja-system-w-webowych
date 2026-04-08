import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review } from '../model/review';

const reviewsApiPrefix = '/api/reviews';

@Injectable({
  providedIn: 'root'
})
export class ReviewsService {

  constructor(private readonly http: HttpClient) { }

  getReviewsForBook(bookId: string | number): Observable<Review[]> {
    return this.http.get<Review[]>(`${reviewsApiPrefix}?forBook=${bookId}`);
  }

  // 4c Dodawanie nowej recenzji
  // Używamy Omit<Review, 'id'>, bo ID zostanie wygenerowane przez backend
  addReview(review: Omit<Review, 'id'>): Observable<Review> {
    return this.http.post<Review>(reviewsApiPrefix, review);
  }
}