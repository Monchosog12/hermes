import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { User } from '../models/usuarios.model';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private readonly cliente = inject(HttpClient);
  private readonly urlbase = 'http://localhost:8080';

  getAll() {
    return this.cliente.get<User[]>(`${this.urlbase}/user/getall`);
  }
}
