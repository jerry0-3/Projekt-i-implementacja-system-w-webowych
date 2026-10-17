import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { describe, beforeEach, it, expect, vi } from 'vitest';

import { RepertoireListComponent } from './repertoire-list';
import { RepertoireService } from '../../services/repertoire';
import {
  PagedRepertoireResponse,
  RepertoireView
} from '../../model/repertoire.model';

describe('RepertoireListComponent', () => {
  let component: RepertoireListComponent;
  let fixture: ComponentFixture<RepertoireListComponent>;

  let repertoireService: {
    getMovies: ReturnType<typeof vi.fn>;
    getRooms: ReturnType<typeof vi.fn>;
    getRepertoire: ReturnType<typeof vi.fn>;
    filterState: {
      page?: number;
      size?: number;
      movieId?: number;
      roomName?: string;
    };
  };

  beforeEach(async () => {
    repertoireService = {
      getMovies: vi.fn(),
      getRooms: vi.fn(),
      getRepertoire: vi.fn(),
      filterState: {}
    };

    await TestBed.configureTestingModule({
      imports: [RepertoireListComponent],
      providers: [
        { provide: RepertoireService, useValue: repertoireService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RepertoireListComponent);
    component = fixture.componentInstance;
  });

  afterEach(() => {
      TestBed.resetTestingModule();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should load dictionaries and repertoire', () => {
      repertoireService.getMovies.mockReturnValue(of([]));
      repertoireService.getRooms.mockReturnValue(of([]));

      repertoireService.getRepertoire.mockReturnValue(
        of({
          _embedded: {
            repertoireViewList: [
              {
                screeningId: 1,
                movieId: 1,
                movieTitle: 'Movie',
                movieDescription: 'blank',
                moviePhotoUrl: '',
                roomName: 'room',
                startTime: '16:00',
                endTime: '16:30',
                durationMinutes: 30
              }
            ]
          },
          page: {
            size: 1,
            totalElements: 1,
            totalPages: 1,
            number: 0
          }
        } as PagedRepertoireResponse)
      );

      component.ngOnInit();

      expect(repertoireService.getMovies).toHaveBeenCalled();
      expect(repertoireService.getRooms).toHaveBeenCalled();
      expect(repertoireService.getRepertoire).toHaveBeenCalled();
    });

    it('should initialize filter state from service', () => {
      repertoireService.getMovies.mockReturnValue(of([]));
      repertoireService.getRooms.mockReturnValue(of([]));

      repertoireService.getRepertoire.mockReturnValue(
        of({
          _embedded: { items: [] },
          page: { number: 2, totalPages: 5 }
        } as any)
      );

      repertoireService.filterState = {
        page: 2,
        size: 20,
        movieId: 5,
        roomName: 'Room A'
      };

      component.ngOnInit();

      expect(component.currentPage()).toBe(2);
      expect(component.pageSize()).toBe(20);
      expect(component.selectedMovieId()).toBe(5);
      expect(component.selectedRoomName()).toBe('Room A');
    });
  });

  describe('loadRepertoire', () => {
    it('should set screenings from response', () => {
      const mockData: RepertoireView[] = [
        {
          screeningId: 1,
          movieId: 1,
          movieTitle: 'Movie',
          movieDescription: 'blank',
          moviePhotoUrl: '',
          roomName: 'room',
          startTime: '16:00',
          endTime: '16:30',
          durationMinutes: 30
        } as RepertoireView,
        {
          screeningId: 2,
          movieId: 2,
          movieTitle: 'Movie2',
          movieDescription: 'blank',
          moviePhotoUrl: '',
          roomName: 'room2',
          startTime: '16:00',
          endTime: '16:30',
          durationMinutes: 30
        } as RepertoireView
      ];

      const response: PagedRepertoireResponse = {
        _embedded: { items: mockData },
        page: { number: 1, totalPages: 3 }
      } as any;

      repertoireService.getRepertoire.mockReturnValue(of(response));

      component.loadRepertoire(1);

      expect(component.screenings().length).toBe(2);
      expect(component.currentPage()).toBe(1);
      expect(component.totalPages()).toBe(3);
      expect(component.isLoading()).toBe(false);
    });

    it('should handle empty response', () => {
      repertoireService.getRepertoire.mockReturnValue(of(null as any));

      component.loadRepertoire(0);

      expect(component.screenings().length).toBe(0);
      expect(component.currentPage()).toBe(0);
      expect(component.totalPages()).toBe(0);
      expect(component.isLoading()).toBe(false);
    });

    it('should handle error and log it', () => {
      const consoleSpy = vi
        .spyOn(console, 'error')
        .mockImplementation(() => {});

      repertoireService.getRepertoire.mockReturnValue(
        throwError(() => new Error('fail'))
      );

      component.loadRepertoire(0);

      expect(consoleSpy).toHaveBeenCalled();
      expect(component.isLoading()).toBe(false);

      consoleSpy.mockRestore();
    });

    it('should update filterState when loading', () => {
      repertoireService.getRepertoire.mockReturnValue(
        of({
          _embedded: { items: [] },
          page: { number: 0, totalPages: 1 }
        } as any)
      );

      component.pageSize.set(10);
      component.selectedMovieId.set(3);
      component.selectedRoomName.set('Room A');

      component.loadRepertoire(2);

      expect(repertoireService.filterState.page).toBe(2);
      expect(repertoireService.filterState.size).toBe(10);
      expect(repertoireService.filterState.movieId).toBe(3);
      expect(repertoireService.filterState.roomName).toBe('Room A');
    });
  });

  describe('pagination', () => {
    beforeEach(() => {
      vi.spyOn(component, 'loadRepertoire').mockImplementation(() => {});
    });

    it('nextPage should increment page', () => {
      component.currentPage.set(1);
      component.totalPages.set(5);

      component.nextPage();

      expect(component.loadRepertoire).toHaveBeenCalledWith(2);
    });

    it('prevPage should decrement page', () => {
      component.currentPage.set(2);

      component.prevPage();

      expect(component.loadRepertoire).toHaveBeenCalledWith(1);
    });

    it('firstPage should go to 0', () => {
      component.currentPage.set(2);

      component.firstPage();

      expect(component.loadRepertoire).toHaveBeenCalledWith(0);
    });

    it('lastPage should go to last page', () => {
      component.currentPage.set(1);
      component.totalPages.set(5);

      component.lastPage();

      expect(component.loadRepertoire).toHaveBeenCalledWith(4);
    });

    it('should not go beyond last page', () => {
      component.currentPage.set(4);
      component.totalPages.set(5);

      component.nextPage();

      expect(component.loadRepertoire).not.toHaveBeenCalledWith(5);
    });

    it('should not go below page 0', () => {
      component.currentPage.set(0);

      component.prevPage();

      expect(component.loadRepertoire).not.toHaveBeenCalledWith(-1);
    });
  });

  describe('handleImageError', () => {
    it('should replace broken image src', () => {
      const event = {
        target: {
          src: ''
        }
      };

      component.handleImageError(event);

      expect(event.target.src).toBe(
        'https://placehold.co/300x450'
      );
    });
  });
});