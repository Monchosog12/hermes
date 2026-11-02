import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Headercomponent } from './components/usercomponentes/headercomponent/headercomponent';
import { Footercomponent } from './components/usercomponentes/footercomponent/footercomponent';
import { Traductorcomponent } from './components/usercomponentes/traductorcomponent/traductorcomponent';
import { Codeeditorcomponent } from './components/usercomponentes/codeeditorcomponent/codeeditorcomponent';
import { Mejoracodigocomponent } from './components/usercomponentes/mejoracodigocomponent/mejoracodigocomponent';
import { FormsModule } from '@angular/forms';
import { Principaladmincomponent } from './components/admincomponents/principaladmincomponent/principaladmincomponent';
import { Historialcomponent } from './components/usercomponentes/historialcomponent/historialcomponent';
import { Headeradmincomponent } from './components/admincomponents/headeradmincomponent/headeradmincomponent';
import { Usuariosadmincomponent } from './components/admincomponents/usuariosadmincomponent/usuariosadmincomponent';
import { Solicitudesadmincomponent } from './components/admincomponents/solicitudesadmincomponent/solicitudesadmincomponent';
@NgModule({
  declarations: [
    App,
    Headercomponent,
    Footercomponent,
    Traductorcomponent,
    Codeeditorcomponent,
    Mejoracodigocomponent,
    Principaladmincomponent,
    Historialcomponent,
    Headeradmincomponent,
    Usuariosadmincomponent,
    Solicitudesadmincomponent,
  ],
  imports: [BrowserModule, AppRoutingModule, FormsModule],
  providers: [provideBrowserGlobalErrorListeners()],
  bootstrap: [App],
})
export class AppModule {}
