import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthResponse } from '../models/authresponse.model';
import { Observable } from 'rxjs';
import { Authlogin } from '../models/authlogin.model';
import { Register } from '../models/register.model';

@Injectable({ providedIn: 'root' })
export class AuthLoginService {
  private cliente: HttpClient = inject(HttpClient);
  private readonly urlbase: string = 'http://localhost:8080';

  iniciarSesion(datos: Authlogin): Observable<AuthResponse> {
    return this.cliente.post<AuthResponse>(`${this.urlbase}/auth/login`, datos);
  }

}
