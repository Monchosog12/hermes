import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { User, UsuarioActualizacion } from '../models/usuarios.model';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private readonly cliente = inject(HttpClient);
  private readonly urlbase = 'http://localhost:8080';

  getAll() {
    return this.cliente.get<User[]>(`${this.urlbase}/user/getall`);
  }

  /** Actualiza un usuario. Si `password` va vacío, el backend conserva la contraseña actual. */
  actualizar(id: number, datos: UsuarioActualizacion) {
    return this.cliente.post(`${this.urlbase}/user/updateuser/${id}`, datos, {
      responseType: 'text',
    });
  }

  /** Elimina un usuario junto con su historial de traducciones. */
  eliminar(id: number) {
    return this.cliente.delete(`${this.urlbase}/user/deletebyid/${id}`, {
      responseType: 'text',
    });
  }
}
