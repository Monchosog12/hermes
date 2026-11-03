import { Component } from '@angular/core';


type Lenguaje = 'javascript' | 'java' | 'python';


@Component({
  selector: 'app-mejoracodigocomponent',
  standalone: false,
  styleUrl: './mejoracodigocomponent.css',
  templateUrl: './mejoracodigocomponent.html',
})
export class Mejoracodigocomponent {
  lenguajeSeleccionado: Lenguaje = 'javascript';

  codigoOriginal = '';
  codigoMejorado = '';
}
