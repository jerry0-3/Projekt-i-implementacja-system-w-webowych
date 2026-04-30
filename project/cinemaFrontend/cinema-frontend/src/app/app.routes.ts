import { Routes } from '@angular/router';
import { RepertoireListComponent } from './repertoire/components/repertoire-list/repertoire-list';
import { SeatSelectionComponent } from './repertoire/components/seat-selection/seat-selection';

export const routes: Routes = [
  // Przekierowanie ze ścieżki głównej bezpośrednio do repertuaru
  { path: '', redirectTo: 'repertoire', pathMatch: 'full' },
  { path: 'repertoire', component: RepertoireListComponent },
  { path: 'repertoire/seats/:id', component: SeatSelectionComponent },
];