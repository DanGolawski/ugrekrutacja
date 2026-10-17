import { Routes } from '@angular/router';
import { ConversionPageComponent } from './conversion-page/conversion-page.component';
import { HistoryPageComponent } from './history-page/history-page.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'conversion' },
  { path: 'conversion', component: ConversionPageComponent },
  { path: 'history', component: HistoryPageComponent },
  { path: '**', redirectTo: 'conversion' }
];
