import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TraduccionModel } from '../models/traduccion.model';
import { AnswertraduccionModel } from '../models/answertraduccion.model';
import { LoginService } from './login.service';

@Injectable({ providedIn: 'root' })
export class TraduccionService {
  private readonly cliente = inject(HttpClient);
  private readonly loginServicio = inject(LoginService);
  private readonly urlbase = 'http://localhost:8080';

  /** Arma el header "Authorization: Bearer <token>" con el token guardado al iniciar sesión. */
  private obtenerHeaders(): HttpHeaders {
    const token = this.loginServicio.obtenerToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  /** Traduce usando el endpoint del modelo indicado (ej. 'cohere', 'nemotron', 'qwen'). */
  traducir(datos: TraduccionModel, endpoint: string) {
    return this.cliente.post<AnswertraduccionModel>(
      `${this.urlbase}/${endpoint}/traduccion`,
      datos,
      { headers: this.obtenerHeaders() }
    );
  }

  traducirCohere(datos: TraduccionModel) {
    return this.cliente.post<AnswertraduccionModel>(
      `${this.urlbase}/cohere/traduccion`,
      datos,
      { headers: this.obtenerHeaders() }
    );
  }

  traducirNemotron(datos: TraduccionModel) {
    return this.cliente.post<AnswertraduccionModel>(
      `${this.urlbase}/nemotron/traduccion`,
      datos,
      { headers: this.obtenerHeaders() }
    );
  }
}
