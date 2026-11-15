import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TraduccionModel } from '../models/traduccion.model';
import { AnswertraduccionModel } from '../models/answertraduccion.model';

@Injectable({ providedIn: 'root' })
export class TraduccionService {
  private readonly cliente = inject(HttpClient);
  private readonly urlbase = 'http://localhost:8080';

  // El token JWT lo agrega automáticamente el tokenInterceptor.
  traducir(datos: TraduccionModel) {
    return this.cliente.post<AnswertraduccionModel>(
      `${this.urlbase}/cohere/traduccion`,
      datos
    );
  }
}
