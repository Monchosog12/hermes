
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

import {
  AdminUser,
  AuditLog,
  DateRange,
  UpdateUserRequest,
  UsageStats
} from '../../models/admin/admin.models';

import { Page } from '../../models/common/page.models';
import { LoginService } from '../login.service';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private readonly http = inject(HttpClient);
  private readonly loginServicio = inject(LoginService);

  private readonly api = `${environment.apiUrl}/admin`;

  /** Arma el header "Authorization: Bearer <token>" con el token guardado al iniciar sesión. */
  private obtenerHeaders(): HttpHeaders {
    const token = this.loginServicio.obtenerToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  users(page: number, size = 10): Observable<Page<AdminUser>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<Page<AdminUser>>(
      `${this.api}/users`,
      { params, headers: this.obtenerHeaders() }
    );
  }

  updateUser(
    id: number,
    body: UpdateUserRequest
  ): Observable<AdminUser> {
    return this.http.patch<AdminUser>(
      `${this.api}/users/${id}`,
      body,
      { headers: this.obtenerHeaders() }
    );
  }

  sendPasswordReset(id: number): Observable<void> {
    return this.http.post<void>(
      `${this.api}/users/${id}/password-reset`,
      {},
      { headers: this.obtenerHeaders() }
    );
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.api}/users/${id}`,
      { headers: this.obtenerHeaders() }
    );
  }

  stats(range: DateRange): Observable<UsageStats> {
    return this.http.get<UsageStats>(
      `${this.api}/stats`,
      { params: this.rangeParams(range), headers: this.obtenerHeaders() }
    );
  }

  audit(page: number, size = 20): Observable<Page<AuditLog>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<Page<AuditLog>>(
      `${this.api}/audit`,
      { params, headers: this.obtenerHeaders() }
    );
  }

  requestsReport(range: DateRange): Observable<Blob> {
    return this.http.get(
      `${this.api}/reports/requests`,
      {
        params: this.rangeParams(range),
        headers: this.obtenerHeaders(),
        responseType: 'blob'
      }
    );
  }

  private rangeParams(range: DateRange): HttpParams {
    return new HttpParams()
      .set('from', range.from)
      .set('to', range.to);
  }
}
