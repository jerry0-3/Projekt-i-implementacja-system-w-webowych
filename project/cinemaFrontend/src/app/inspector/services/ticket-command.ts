import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TicketDetailsResponse } from '../model/inspector.model';

@Injectable({
  providedIn: 'root'
})
export class TicketCommandService {
  private readonly COMMAND_API_URL = '/api/commands/tickets';

  constructor(private http: HttpClient) {}

  scanTicket(uniqueCode: string): Observable<TicketDetailsResponse> {
    return this.http.get<TicketDetailsResponse>(`${this.COMMAND_API_URL}/${uniqueCode}`);
  }

  cancelTicket(uniqueCode: string): Observable<void> {
    return this.http.post<void>(`${this.COMMAND_API_URL}/${uniqueCode}/consume`, {});
  }
}