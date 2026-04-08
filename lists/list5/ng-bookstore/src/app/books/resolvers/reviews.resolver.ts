import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from '@angular/router';
import { ReviewsService } from '../services/reviews.service';
import { Review } from '../model/review';

export const reviewsResolver: ResolveFn<Review[]> = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
) => {
  const bookId = route.paramMap.get('bookId')!;
  return inject(ReviewsService).getReviewsForBook(bookId);
};