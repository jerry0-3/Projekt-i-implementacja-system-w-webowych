import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagedRepertoireResponse, MovieDictionaryDto, ScreeningSeatView } from '../model/repertoire.model';

@Injectable({
  providedIn: 'root'
})
export class RepertoireService {
  private readonly BASE_API_URL = '/api/queries';

  public filterState = {
    page: 0,
    size: 10,
    movieId: null as number | null,
    roomName: null as string | null
  };

  constructor(private http: HttpClient) {}

  getMovies(): Observable<MovieDictionaryDto[]> {
    return this.http.get<MovieDictionaryDto[]>('/api/queries/dictionaries/movies');
  }

  getRooms(): Observable<string[]> {
    return this.http.get<string[]>('/api/queries/dictionaries/rooms');
  }

  getRepertoire(page: number = 0, size: number = 10, movieId?: number | null, roomName?: string | null): Observable<PagedRepertoireResponse> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'startTime,asc');

    if (movieId) params = params.set('movieId', movieId.toString());
    if (roomName) params = params.set('roomName', roomName);

    return this.http.get<PagedRepertoireResponse>(`${this.BASE_API_URL}/repertoire`, { params });
  }

  getSeatsForScreening(screeningId: number): Observable<ScreeningSeatView[]> {
    return this.http.get<ScreeningSeatView[]>(`${this.BASE_API_URL}/screenings/${screeningId}/seats`);
  }
}