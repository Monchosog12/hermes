import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Traductorcomponent} from './components/usercomponentes/traductorcomponent/traductorcomponent';
import {Mejoracodigocomponent} from './components/usercomponentes/mejoracodigocomponent/mejoracodigocomponent';
import {Historialcomponent} from './components/usercomponentes/historialcomponent/historialcomponent';
import {Principaladmincomponent} from './components/admincomponents/principaladmincomponent/principaladmincomponent';
import {Usuariosadmincomponent} from './components/admincomponents/usuariosadmincomponent/usuariosadmincomponent';
import {
  Solicitudesadmincomponent
} from './components/admincomponents/solicitudesadmincomponent/solicitudesadmincomponent';
import {Reportesadmincomponent} from './components/admincomponents/reportesadmincomponent/reportesadmincomponent';

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
    children: [{path: '', redirectTo: 'principaladmin', pathMatch: 'full'},
      {path: 'principaladmin', component: Principaladmincomponent},
      {path: 'usuarios', component: Usuariosadmincomponent},
      {path: 'solicitudes', component: Solicitudesadmincomponent},
      {path: 'reportes', component: Reportesadmincomponent},
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
