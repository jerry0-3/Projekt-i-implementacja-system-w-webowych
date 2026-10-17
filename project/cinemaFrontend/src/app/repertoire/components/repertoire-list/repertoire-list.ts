import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RepertoireService } from '../../services/repertoire';
import { PagedRepertoireResponse, RepertoireView, MovieDictionaryDto } from '../../model/repertoire.model';
import { catchError, finalize } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-repertoire-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './repertoire-list.html',
  styleUrls: ['./repertoire-list.scss']
})
export class RepertoireListComponent implements OnInit {

  screenings = signal<RepertoireView[]>([]);
  currentPage = signal<number>(0);
  totalPages = signal<number>(0);
  isLoading = signal<boolean>(true);
  pageSize = signal<number>(10);

  moviesList = signal<MovieDictionaryDto[]>([]);
  roomsList = signal<string[]>([]);
  selectedMovieId = signal<number | null>(null);
  selectedRoomName = signal<string | null>(null);

  constructor(private repertoireService: RepertoireService) {}

  ngOnInit(): void {
    this.loadDictionaries();

    const state = this.repertoireService.filterState;
    this.currentPage.set(state.page);
    this.pageSize.set(state.size);
    this.selectedMovieId.set(state.movieId);
    this.selectedRoomName.set(state.roomName);

    this.loadRepertoire(this.currentPage());
  }

  loadDictionaries(): void {
    this.repertoireService.getMovies().subscribe(res => this.moviesList.set(res));
    this.repertoireService.getRooms().subscribe(res => this.roomsList.set(res));
  }

  loadRepertoire(page: number = 0): void {
    this.isLoading.set(true);

    this.repertoireService.filterState = {
      page: page,
      size: this.pageSize(),
      movieId: this.selectedMovieId(),
      roomName: this.selectedRoomName()
    };

    this.repertoireService.getRepertoire(page, this.pageSize(), this.selectedMovieId(), this.selectedRoomName()).pipe(
      catchError(err => {
        console.error('Błąd pobierania repertuaru', err);
        return of(null);
      }),
      finalize(() => this.isLoading.set(false))
    ).subscribe((response: PagedRepertoireResponse | null) => {
      if (response && response._embedded) {
         const data = Object.values(response._embedded)[0] as RepertoireView[];
         this.screenings.set(data);
         this.currentPage.set(response.page.number);
         this.totalPages.set(response.page.totalPages);
      } else {
         this.screenings.set([]);
         this.currentPage.set(0);
         this.totalPages.set(0);
      }
    });
  }

  onFilterChange(): void {
    this.loadRepertoire(0);
  }

  nextPage(): void {
    if (this.currentPage() < this.totalPages() - 1) this.loadRepertoire(this.currentPage() + 1);
  }

  prevPage(): void {
    if (this.currentPage() > 0) this.loadRepertoire(this.currentPage() - 1);
  }

  firstPage(): void {
    if (this.currentPage() !== 0) this.loadRepertoire(0);
  }

  lastPage(): void {
    if (this.currentPage() !== this.totalPages() - 1) this.loadRepertoire(this.totalPages() - 1);
  }

  handleImageError(event: any) {
    event.target.src = 'https://placehold.co/300x450';
  }
}