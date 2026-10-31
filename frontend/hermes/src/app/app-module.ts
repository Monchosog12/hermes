import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Headercomponent } from './components/headercomponent/headercomponent';
import { Footercomponent } from './components/footercomponent/footercomponent';
import { Traductorcomponent } from './components/traductorcomponent/traductorcomponent';
import { Codeeditorcomponent } from './components/codeeditorcomponent/codeeditorcomponent';
import { Mejoracodigocomponent } from './components/mejoracodigocomponent/mejoracodigocomponent';
import {FormsModule} from '@angular/forms';
@NgModule({
  declarations: [
    App,
    Headercomponent,
    Footercomponent,
    Traductorcomponent,
    Codeeditorcomponent,
    Mejoracodigocomponent,
  ],
  imports: [BrowserModule, AppRoutingModule, FormsModule],
  providers: [provideBrowserGlobalErrorListeners()],
  bootstrap: [App],
})
export class AppModule {}
