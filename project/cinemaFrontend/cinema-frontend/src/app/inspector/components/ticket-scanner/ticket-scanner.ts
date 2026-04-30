import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TicketCommandService } from '../../services/ticket-command';
import { TicketDetailsResponse } from '../../model/inspector.model';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-ticket-scanner',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ticket-scanner.html',
  styleUrls: ['./ticket-scanner.scss']
})
export class TicketScannerComponent {
  // Wpisany przez biletera kod
  ticketCode = signal<string>('');
  
  // Zwrócone z backendu szczegóły
  ticketDetails = signal<TicketDetailsResponse | null>(null);
  
  errorMessage = signal<string>('');
  isLoading = signal<boolean>(false);

  constructor(
    private ticketService: TicketCommandService,
    private authService: AuthService
  ) {}

  scanTicket(): void {
    const code = this.ticketCode().trim();
    if (!code) return;

    this.isLoading.set(true);
    this.errorMessage.set('');
    this.ticketDetails.set(null);

    this.ticketService.scanTicket(code).subscribe({
      next: (details) => {
        this.ticketDetails.set(details);
        this.isLoading.set(false);
        // Czyścimy pole wejściowe po udanym skanie
        this.ticketCode.set(''); 
      },
      error: (err) => {
        console.error('Błąd skanowania', err);
        // Jeśli backend rzuci np. 404
        this.errorMessage.set('Nie znaleziono biletu o podanym kodzie.');
        this.isLoading.set(false);
      }
    });
  }

  cancelTicket(codeToCancel: string): void {
    if (!confirm('Czy na pewno chcesz skasować ten bilet?')) return;

    this.isLoading.set(true);
    this.ticketService.cancelTicket(codeToCancel).subscribe({
      next: () => {
        // Po pomyślnym skasowaniu odświeżamy widok, skanując go jeszcze raz
        this.ticketCode.set(codeToCancel);
        this.scanTicket();
      },
      error: (err) => {
        this.errorMessage.set('Błąd podczas kasowania biletu.');
        this.isLoading.set(false);
      }
    });
  }

  logout(): void {
    this.authService.logout();
  }
}