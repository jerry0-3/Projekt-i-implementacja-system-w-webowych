import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { SeatSelectionComponent } from './seat-selection';
import { RepertoireService } from '../../services/repertoire';
import { TicketService } from '../../services/ticket';
import { ScreeningSeatView } from '../../model/repertoire.model';

describe('SeatSelectionComponent', () => {
  let component: SeatSelectionComponent;
  let fixture: ComponentFixture<SeatSelectionComponent>;

  let repertoireService: {
    getSeatsForScreening: ReturnType<typeof vi.fn>;
  };

  let ticketService: {
    buyTicket: ReturnType<typeof vi.fn>;
  };

  const routeMock = {
    snapshot: {
      paramMap: {
        get: vi.fn()
      }
    }
  };

  const routerMock = {
    navigate: vi.fn()
  };

  beforeEach(async () => {
    repertoireService = {
      getSeatsForScreening: vi.fn()
    };

    ticketService = {
      buyTicket: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [SeatSelectionComponent],
      providers: [
        { provide: ActivatedRoute, useValue: routeMock },
        { provide: Router, useValue: routerMock },
        { provide: RepertoireService, useValue: repertoireService },
        { provide: TicketService, useValue: ticketService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SeatSelectionComponent);
    component = fixture.componentInstance;
  });

  beforeEach(() => {
    routeMock.snapshot.paramMap.get.mockReturnValue('1');
  });

  afterEach(() => {
    TestBed.resetTestingModule();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should load seats when id exists in route', () => {
      const loadSeatsSpy = vi
        .spyOn(component, 'loadSeats')
        .mockImplementation(() => {});

      component.ngOnInit();

      expect(component.screeningId()).toBe(1);
      expect(loadSeatsSpy).toHaveBeenCalled();
    });

    it('should not load seats when id is missing', () => {
      routeMock.snapshot.paramMap.get.mockReturnValue(null);

      const loadSeatsSpy = vi
        .spyOn(component, 'loadSeats')
        .mockImplementation(() => {});

      component.ngOnInit();

      expect(loadSeatsSpy).not.toHaveBeenCalled();
    });
  });

  describe('loadSeats', () => {
    it('should group seats into rows and set state', () => {
      const mockSeats: ScreeningSeatView[] = [
        {
          id: 1,
          rowNumber: 1,
          seatNumber: 1,
          available: true
        } as any,
        {
          id: 2,
          rowNumber: 1,
          seatNumber: 2,
          available: true
        } as any,
        {
          id: 3,
          rowNumber: 2,
          seatNumber: 1,
          available: true
        } as any
      ];

      repertoireService.getSeatsForScreening.mockReturnValue(
        of(mockSeats)
      );

      component.screeningId.set(1);

      component.loadSeats();

      expect(component.isLoading()).toBe(false);
      expect(component.rows().length).toBe(2);
      expect(component.maxSeatsInRow()).toBe(2);
    });

    it('should handle error and stop loading', () => {
      const consoleSpy = vi
        .spyOn(console, 'error')
        .mockImplementation(() => {});

      repertoireService.getSeatsForScreening.mockReturnValue(
        throwError(() => new Error('fail'))
      );

      component.screeningId.set(1);

      component.loadSeats();

      expect(consoleSpy).toHaveBeenCalled();
      expect(component.isLoading()).toBe(false);

      consoleSpy.mockRestore();
    });

    it('should return early when no screeningId', () => {
      component.screeningId.set(null);

      component.loadSeats();

      expect(
        repertoireService.getSeatsForScreening
      ).not.toHaveBeenCalled();
    });
  });

  describe('toggleSeat', () => {
    it('should not toggle unavailable seat', () => {
      const seat = {
        id: 1,
        available: false
      } as ScreeningSeatView;

      component.toggleSeat(seat);

      expect(component.selectedSeats().length).toBe(0);
    });

    it('should select a seat', () => {
      const seat = {
        id: 1,
        available: true
      } as ScreeningSeatView;

      component.toggleSeat(seat);

      expect(component.selectedSeats().length).toBe(1);
    });

    it('should deselect a seat if already selected', () => {
      const seat = {
        id: 1,
        available: true
      } as ScreeningSeatView;

      component.toggleSeat(seat);
      component.toggleSeat(seat);

      expect(component.selectedSeats().length).toBe(0);
    });

    it('should enforce max 10 seats limit', () => {
      const seats = Array.from({ length: 10 }, (_, i) => ({
        id: i + 1,
        available: true
      })) as ScreeningSeatView[];

      seats.forEach(seat => component.toggleSeat(seat));

      const extraSeat = {
        id: 999,
        available: true
      } as ScreeningSeatView;

      const alertSpy = vi
        .spyOn(window, 'alert')
        .mockImplementation(() => {});

      component.toggleSeat(extraSeat);

      expect(alertSpy).toHaveBeenCalled();
      expect(component.selectedSeats().length).toBe(10);

      alertSpy.mockRestore();
    });
  });

  describe('isSelected', () => {
    it('should return true for selected seat', () => {
      const seat = { id: 1 } as ScreeningSeatView;

      component.selectedSeats.set([seat]);

      expect(component.isSelected(seat)).toBe(true);
    });

    it('should return false for non-selected seat', () => {
      const seat = { id: 1 } as ScreeningSeatView;

      component.selectedSeats.set([]);

      expect(component.isSelected(seat)).toBe(false);
    });
  });

  describe('isAisle', () => {
    it('should detect aisle seat correctly', () => {
      component.maxSeatsInRow.set(10);

      expect(component.isAisle(5)).toBe(true);
    });

    it('should return false for non-aisle seat', () => {
      component.maxSeatsInRow.set(10);

      expect(component.isAisle(2)).toBe(false);
    });
  });

  describe('buyTickets', () => {
    it('should not buy when no screeningId', () => {
      component.screeningId.set(null);

      component.buyTickets();

      expect(ticketService.buyTicket).not.toHaveBeenCalled();
    });

    it('should not buy when no seats selected', () => {
      component.screeningId.set(1);
      component.selectedSeats.set([]);

      component.buyTickets();

      expect(ticketService.buyTicket).not.toHaveBeenCalled();
    });

    it('should call ticketService with correct payload', () => {
      component.screeningId.set(1);

      component.selectedSeats.set([
        { originalSeatId: 10 } as any,
        { originalSeatId: 11 } as any
      ]);

      ticketService.buyTicket.mockReturnValue(
        of({ uniqueCode: 'ABC', message: 'ok' })
      );

      component.buyTickets();

      expect(ticketService.buyTicket).toHaveBeenCalledWith({
        screeningId: 1,
        seatIds: [10, 11]
      });
    });

    it('should set purchased ticket code on success', () => {
      component.screeningId.set(1);

      component.selectedSeats.set([
        { originalSeatId: 10 } as any
      ]);

      ticketService.buyTicket.mockReturnValue(
        of({ uniqueCode: 'XYZ', message: 'ok' })
      );

      component.buyTickets();

      expect(component.purchasedTicketCode()).toBe('XYZ');
      expect(component.isPurchasing()).toBe(false);
    });

    it('should handle error, reload seats and clear selection', () => {
      const consoleSpy = vi
        .spyOn(console, 'error')
        .mockImplementation(() => {});

      const alertSpy = vi
        .spyOn(window, 'alert')
        .mockImplementation(() => {});

      const loadSeatsSpy = vi
        .spyOn(component, 'loadSeats')
        .mockImplementation(() => {});

      component.screeningId.set(1);

      component.selectedSeats.set([
        { originalSeatId: 10 } as any
      ]);

      ticketService.buyTicket.mockReturnValue(
        throwError(() => new Error('fail'))
      );

      component.buyTickets();

      expect(alertSpy).toHaveBeenCalled();
      expect(loadSeatsSpy).toHaveBeenCalled();
      expect(component.selectedSeats().length).toBe(0);
      expect(component.isPurchasing()).toBe(false);

      consoleSpy.mockRestore();
      alertSpy.mockRestore();
    });
  });
});