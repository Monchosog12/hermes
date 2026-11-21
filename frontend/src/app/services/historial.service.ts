import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {
  TraduccionHistorial,
  TraduccionHistorialActualizacion,
} from '../models/history/traduccionhistorial.model';

/**
 * Historial de traducciones del usuario con sesión iniciada.
 * El token lo agrega el authInterceptor; el backend solo devuelve y modifica
 * las traducciones del dueño del token.
 */
@Injectable({ providedIn: 'root' })
export class HistorialService {
  private readonly cliente = inject(HttpClient);
  private readonly urlbase = 'http://localhost:8080/historial';

  listar() {
    return this.cliente.get<TraduccionHistorial[]>(this.urlbase);
  }

  actualizar(id: number, datos: TraduccionHistorialActualizacion) {
    return this.cliente.put(`${this.urlbase}/actualizar/${id}`, datos, { responseType: 'text' });
  }

  eliminar(id: number) {
    return this.cliente.delete(`${this.urlbase}/eliminar/${id}`, { responseType: 'text' });
  }
}
