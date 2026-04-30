export interface SeatInfoDto {
    rowNumber: number;
    seatNumber: number;
  }
  
  export interface TicketDetailsResponse {
    status: string; // NIE ISTNIEJE, WAŻNY, SKASOWANY, NIEWAŻNY
    totalSeats: number;
    roomName: string;
    movieTitle: string;
    seats: SeatInfoDto[];
  }