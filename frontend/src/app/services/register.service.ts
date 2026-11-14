import { inject, Injectable, Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RegisterModel } from '../models/register.model';
import { AnswerregisterModel } from '../models/answerregister.model';

@Injectable({ providedIn: 'root' })
export class RegisterService {
  private readonly cliente = inject(HttpClient);
  private readonly urlbase = 'http://localhost:8080';

  register(datos: RegisterModel) {
    return this.cliente.post<AnswerregisterModel>(
      `${this.urlbase}/auth/register`,
      datos,
      {observe: 'response'}
    );
  }
}
