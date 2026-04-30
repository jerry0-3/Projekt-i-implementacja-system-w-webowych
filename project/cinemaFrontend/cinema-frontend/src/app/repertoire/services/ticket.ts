import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface BuyTicketCommand {
  screeningId: number;
  seatIds: number[];
}

// Dodajemy interfejs dla odpowiedzi z backendu
export interface BuyTicketResponse {
  uniqueCode: string;
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  private readonly COMMAND_API_URL = '/api/commands/tickets';

  constructor(private http: HttpClient) {}

  // Zwracamy obiekt JSON zamiast stringa
  buyTicket(command: BuyTicketCommand): Observable<BuyTicketResponse> {
    return this.http.post<BuyTicketResponse>(this.COMMAND_API_URL, command);
  }
}