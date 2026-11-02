import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Traductorcomponent} from './components/usercomponentes/traductorcomponent/traductorcomponent';
import {Mejoracodigocomponent} from './components/usercomponentes/mejoracodigocomponent/mejoracodigocomponent';
import {Historialcomponent} from './components/usercomponentes/historialcomponent/historialcomponent';

const routes: Routes = [
  {
    path: 'usuario',
    children: [{path: '', redirectTo: 'traductor', pathMatch: 'full'},
      {path: 'traductor', component: Traductorcomponent},
      {path: 'mejoracodigo', component: Mejoracodigocomponent},
      {path: 'historial', component: Historialcomponent},
    ]
  },

  {
    path: 'admin',
    children: [
      {path: 'Principaladmin', component: Traductorcomponent},
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
