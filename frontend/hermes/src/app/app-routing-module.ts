import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Traductorcomponent} from './components/traductorcomponent/traductorcomponent';

const routes: Routes = [
  { path: '', redirectTo: 'traductor', pathMatch: 'full' },
  { path: 'traductor', component: Traductorcomponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
