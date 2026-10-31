import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Traductorcomponent} from './components/traductorcomponent/traductorcomponent';
import {Mejoracodigocomponent} from './components/mejoracodigocomponent/mejoracodigocomponent';

const routes: Routes = [
  { path: '', redirectTo: 'traductor', pathMatch: 'full' },
  { path: 'traductor', component: Traductorcomponent },
  { path: 'mejoracodigo', component: Mejoracodigocomponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
