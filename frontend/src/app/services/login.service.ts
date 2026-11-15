import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { LoginModel } from '../models/login.model';
import { AnswerloginModel } from '../models/answerlogin.model';

const CLAVE_TOKEN = 'hermes.token';
const CLAVE_ROL = 'hermes.rol';
const CLAVE_USUARIO = 'hermes.usuario';

@Injectable({ providedIn: 'root' })
export class LoginService {
  private readonly cliente = inject(HttpClient);
  private readonly urlbase = 'http://localhost:8080';

  login(datos: LoginModel) {
    return this.cliente
      .post<AnswerloginModel>(`${this.urlbase}/auth/login`, datos)
      .pipe(tap((respuesta) => this.guardarSesion(respuesta, datos.username)));
  }

  cerrarSesion(): void {
    sessionStorage.removeItem(CLAVE_TOKEN);
    sessionStorage.removeItem(CLAVE_ROL);
    sessionStorage.removeItem(CLAVE_USUARIO);
  }

  obtenerToken(): string | null {
    return sessionStorage.getItem(CLAVE_TOKEN);
  }

  obtenerRol(): string | null {
    return sessionStorage.getItem(CLAVE_ROL);
  }

  obtenerUsuario(): string | null {
    return sessionStorage.getItem(CLAVE_USUARIO);
  }

  estaLogueado(): boolean {
    return this.obtenerToken() !== null;
  }

  private guardarSesion(respuesta: AnswerloginModel, username: string): void {
    sessionStorage.setItem(CLAVE_TOKEN, respuesta.token);
    sessionStorage.setItem(CLAVE_ROL, respuesta.role ?? '');
    sessionStorage.setItem(CLAVE_USUARIO, username);
  }
}
