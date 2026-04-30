import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { QRCodeComponent } from 'angularx-qrcode';
import { RepertoireService } from '../../services/repertoire';
import { TicketService, BuyTicketCommand, BuyTicketResponse } from '../../services/ticket';
import { ScreeningSeatView } from '../../model/repertoire.model';

interface SeatRow {
  rowNumber: number;
  seats: ScreeningSeatView[];
}

@Component({
  selector: 'app-seat-selection',
  standalone: true,
  imports: [CommonModule, RouterModule, QRCodeComponent],
  templateUrl: './seat-selection.html',
  styleUrls: ['./seat-selection.scss']
})
export class SeatSelectionComponent implements OnInit {
  
  screeningId = signal<number | null>(null);
  isLoading = signal<boolean>(true);
  
  // Zgrupowane rzędy foteli
  rows = signal<SeatRow[]>([]);
  
  // Lista zaznaczonych foteli (przechowujemy całe obiekty, aby móc je podsumować)
  selectedSeats = signal<ScreeningSeatView[]>([]);

  // Wyliczamy maksymalną liczbę miejsc w rzędzie, by wiedzieć, gdzie zrobić "alejkę"
  maxSeatsInRow = signal<number>(0);

  isPurchasing = signal<boolean>(false);
  purchasedTicketCode = signal<string | null>(null);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private repertoireService: RepertoireService,
    private ticketService: TicketService
  ) {}

  ngOnInit(): void {
    // Odczytujemy ID seansu z URL (np. /repertoire/seats/73)
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.screeningId.set(Number(idParam));
      this.loadSeats();
    }
  }

  loadSeats(): void {
    const id = this.screeningId();
    if (!id) return;

    this.repertoireService.getSeatsForScreening(id).subscribe({
      next: (seats) => {
        // Obliczamy najdłuższy rząd (dla alejki)
        const max = Math.max(...seats.map(s => s.seatNumber));
        this.maxSeatsInRow.set(max);

        // Grupujemy płaską listę po numerze rzędu
        const groupedMap = new Map<number, ScreeningSeatView[]>();
        seats.forEach(seat => {
          if (!groupedMap.has(seat.rowNumber)) {
            groupedMap.set(seat.rowNumber, []);
          }
          groupedMap.get(seat.rowNumber)!.push(seat);
        });

        // Konwertujemy mapę na posortowaną tablicę rzędów
        const rowsArray = Array.from(groupedMap.entries())
          .map(([rowNumber, seats]) => ({
            rowNumber,
            // Sortujemy miejsca wewnątrz rzędu od lewej do prawej
            seats: seats.sort((a, b) => a.seatNumber - b.seatNumber)
          }))
          // Sortujemy rzędy od pierwszego do ostatniego
          .sort((a, b) => a.rowNumber - b.rowNumber);

        this.rows.set(rowsArray);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Błąd pobierania miejsc', err);
        this.isLoading.set(false);
      }
    });
  }

  toggleSeat(seat: ScreeningSeatView): void {
    if (!seat.available) return;

    const currentSelected = this.selectedSeats();
    const isAlreadySelected = currentSelected.find(s => s.id === seat.id);

    if (isAlreadySelected) {
      // Odznaczamy
      this.selectedSeats.set(currentSelected.filter(s => s.id !== seat.id));
    } else {
      // Zaznaczamy (limit to np. 10 biletów na transakcję)
      if (currentSelected.length < 10) {
        this.selectedSeats.set([...currentSelected, seat]);
      } else {
        alert('Możesz wybrać maksymalnie 10 miejsc na raz.');
      }
    }
  }

  isSelected(seat: ScreeningSeatView): boolean {
    return this.selectedSeats().some(s => s.id === seat.id);
  }

  // Funkcja zwracająca 'true' dla miejsca, po którym ma być alejka (środek rzędu)
  isAisle(seatNumber: number): boolean {
    const half = Math.floor(this.maxSeatsInRow() / 2);
    return seatNumber === half;
  }

  buyTickets(): void {
    const sId = this.screeningId();
    const seats = this.selectedSeats();

    if (!sId || seats.length === 0) return;

    this.isPurchasing.set(true);

    const command: BuyTicketCommand = {
      screeningId: sId,
      seatIds: seats.map(s => s.originalSeatId) 
    };

    // Używamy nowego typu odpowiedzi
    this.ticketService.buyTicket(command).subscribe({
      next: (response: BuyTicketResponse) => {
        // Z obiektu wyciągamy tylko sam uniqueCode!
        this.purchasedTicketCode.set(response.uniqueCode);
        this.isPurchasing.set(false);
      },
      error: (err) => {
        console.error('Błąd podczas kupowania biletu:', err);
        alert('Niestety, wystąpił problem. Ktoś mógł zająć te miejsca ułamek sekundy przed Tobą!');
        this.isPurchasing.set(false);
        this.loadSeats(); 
        this.selectedSeats.set([]);
      }
    });
  }
}