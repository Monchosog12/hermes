import { inject, Injectable, Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RegisterModel } from '../models/register.model';

@Injectable({ providedIn: 'root' })
export class RegisterService {
  private readonly cliente = inject(HttpClient);
  private readonly urlbase = 'http://localhost:8080';

  register(datos: RegisterModel) {
    // El backend responde texto plano ("Usuario registrado exitosamente"), no JSON.
    return this.cliente.post(
      `${this.urlbase}/auth/register`,
      datos,
      { observe: 'response', responseType: 'text' }
    );
  }
}
