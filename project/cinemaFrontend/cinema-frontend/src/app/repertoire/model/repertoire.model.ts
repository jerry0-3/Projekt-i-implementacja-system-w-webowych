export interface RepertoireView {
    screeningId: number;
    movieId: number;
    movieTitle: string;
    movieDescription: string;
    moviePhotoUrl: string;
    roomName: string;
    startTime: string;
    endTime: string;
    durationMinutes: number;
  }
  
  export interface ScreeningSeatView {
    id: number;
    screeningId: number;
    originalSeatId: number;
    rowNumber: number;
    seatNumber: number;
    isAvailable: boolean;
  }
  
  // Model HATEOAS do paginacji (uproszczony)
  export interface PagedRepertoireResponse {
    _embedded?: {
      repertoireViewList: RepertoireView[];
    };
    page: {
      size: number;
      totalElements: number;
      totalPages: number;
      number: number;
    };
  }