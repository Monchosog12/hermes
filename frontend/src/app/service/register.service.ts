import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Register } from '../models/register.model';
import { Observable } from 'rxjs';
import { AnswerregisterModel } from '../models/answerregister.model';

@Injectable({ providedIn: 'root' })
export class RegisterService {
  private cliente: HttpClient = inject(HttpClient);
  private readonly urlbase: string = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  registro(datos: Register): Observable<AnswerregisterModel>{
    return this.http.post<AnswerregisterModel>(`${this.urlbase}/auth/register`, datos);
  }
}

