import { Routes } from '@angular/router';
import { RepertoireListComponent } from './repertoire/components/repertoire-list/repertoire-list';
import { SeatSelectionComponent } from './repertoire/components/seat-selection/seat-selection';
import { LoginComponent } from './inspector/components/login/login';
import { TicketScannerComponent } from './inspector/components/ticket-scanner/ticket-scanner';
import { authGuard } from './inspector/guards/auth-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'repertoire', pathMatch: 'full' },
  { path: 'repertoire', component: RepertoireListComponent },
  { path: 'repertoire/seats/:id', component: SeatSelectionComponent },

  { path: 'inspector/login', component: LoginComponent },
  { path: 'inspector/scanner', component: TicketScannerComponent, canActivate: [authGuard] },
];