import { Component } from '@angular/core';
import { LanguageCode } from '../../../models/ai/ai.models';


@Component({
  selector: 'app-mejoracodigocomponent',
  standalone: false,
  styleUrl: './mejoracodigocomponent.css',
  templateUrl: './mejoracodigocomponent.html',
})
export class Mejoracodigocomponent {
  lenguajeSeleccionado: LanguageCode = 'JAVASCRIPT';

  codigoOriginal = '';
  codigoMejorado = '';
}
