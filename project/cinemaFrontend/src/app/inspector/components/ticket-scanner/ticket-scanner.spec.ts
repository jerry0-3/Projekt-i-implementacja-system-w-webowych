import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';

import { TicketScannerComponent } from './ticket-scanner';
import { TicketCommandService } from '../../services/ticket-command';
import { AuthService } from '../../services/auth';
import {
  SeatInfoDto,
  TicketDetailsResponse
} from '../../model/inspector.model';

describe('TicketScannerComponent', () => {
  let component: TicketScannerComponent;
  let fixture: ComponentFixture<TicketScannerComponent>;

  let ticketService: {
    scanTicket: ReturnType<typeof vi.fn>;
    cancelTicket: ReturnType<typeof vi.fn>;
  };

  let authService: {
    logout: ReturnType<typeof vi.fn>;
  };

  const createMockTicket = (): TicketDetailsResponse => ({
    status: 'WAŻNY',
    totalSeats: 1,
    roomName: 'Sala 6 - 3D',
    movieTitle: 'Diuna: Część Druga',
    seats: [
      {
        seatNumber: 1,
        rowNumber: 1
      } as SeatInfoDto
    ]
  });

  beforeEach(async () => {
    ticketService = {
      scanTicket: vi.fn().mockReturnValue(of(null)),
      cancelTicket: vi.fn().mockReturnValue(of(void 0))
    };

    authService = {
      logout: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [TicketScannerComponent],
      providers: [
        { provide: TicketCommandService, useValue: ticketService },
        { provide: AuthService, useValue: authService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TicketScannerComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('scanTicket', () => {
    it('should not call service when ticket code is empty', () => {
      component.ticketCode.set('');

      component.scanTicket();

      expect(ticketService.scanTicket).not.toHaveBeenCalled();
    });

    it('should trim ticket code before scanning', () => {
      ticketService.scanTicket.mockReturnValue(of(createMockTicket()));

      component.ticketCode.set('  ABC123  ');
      component.scanTicket();

      expect(ticketService.scanTicket).toHaveBeenCalledWith('ABC123');
    });

    it('should set ticket details on successful scan', () => {
      const mock = createMockTicket();
      ticketService.scanTicket.mockReturnValue(of(mock));

      component.ticketCode.set('ABC123');
      component.scanTicket();

      expect(component.ticketDetails()).toEqual(mock);
      expect(component.errorMessage()).toBe('');
      expect(component.isLoading()).toBe(false);
    });

    it('should clear previous error before scanning', () => {
      const mock = createMockTicket();
      ticketService.scanTicket.mockReturnValue(of(mock));

      component.errorMessage.set('Old error');

      component.ticketCode.set('ABC123');
      component.scanTicket();

      expect(component.errorMessage()).toBe('');
    });

    it('should handle scan error', () => {
      vi.spyOn(console, 'error').mockImplementation(() => {});

      ticketService.scanTicket.mockReturnValue(
        throwError(() => new Error('Scan failed'))
      );

      component.ticketCode.set('INVALID');
      component.scanTicket();

      expect(ticketService.scanTicket).toHaveBeenCalledWith('INVALID');
      expect(component.errorMessage()).toBe(
        'Nie znaleziono biletu o podanym kodzie.'
      );
      expect(component.isLoading()).toBe(false);
    });
  });

  describe('cancelTicket', () => {
    it('should not call service when confirmation is rejected', () => {
      vi.spyOn(window, 'confirm').mockReturnValue(false);

      component.cancelTicket('ABC123');

      expect(ticketService.cancelTicket).not.toHaveBeenCalled();
    });

    it('should call service when confirmed', () => {
      vi.spyOn(window, 'confirm').mockReturnValue(true);
      ticketService.cancelTicket.mockReturnValue(of(void 0));

      component.cancelTicket('ABC123');

      expect(ticketService.cancelTicket).toHaveBeenCalledWith('ABC123');
    });

    it('should clear state after successful cancellation', () => {
      vi.spyOn(window, 'confirm').mockReturnValue(true);
      ticketService.cancelTicket.mockReturnValue(of(void 0));

      component.ticketCode.set('ABC123');

      component.cancelTicket('ABC123');

      fixture.detectChanges();
      expect(component.ticketCode()).toBe("");
    });

    it('should handle cancel error', () => {
      vi.spyOn(window, 'confirm').mockReturnValue(true);

      ticketService.cancelTicket.mockReturnValue(
        throwError(() => new Error('Cancel failed'))
      );

      component.cancelTicket('ABC123');

      expect(component.errorMessage()).toBe(
        'Błąd podczas kasowania biletu.'
      );
      expect(component.isLoading()).toBe(false);
    });
  });

  describe('logout', () => {
    it('should call authService logout', () => {
      component.logout();

      expect(authService.logout).toHaveBeenCalled();
    });
  });
});