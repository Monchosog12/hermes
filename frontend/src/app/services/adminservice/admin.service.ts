
import { HttpClient, HttpParams } from '@angular/common/http';
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

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private readonly http = inject(HttpClient);

  private readonly api = `${environment.apiUrl}/admin`;

  users(page: number, size = 10): Observable<Page<AdminUser>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<Page<AdminUser>>(
      `${this.api}/users`,
      { params }
    );
  }

  updateUser(
    id: number,
    body: UpdateUserRequest
  ): Observable<AdminUser> {
    return this.http.patch<AdminUser>(
      `${this.api}/users/${id}`,
      body
    );
  }

  sendPasswordReset(id: number): Observable<void> {
    return this.http.post<void>(
      `${this.api}/users/${id}/password-reset`,
      {}
    );
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.api}/users/${id}`
    );
  }

  stats(range: DateRange): Observable<UsageStats> {
    return this.http.get<UsageStats>(
      `${this.api}/stats`,
      { params: this.rangeParams(range) }
    );
  }

  audit(page: number, size = 20): Observable<Page<AuditLog>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<Page<AuditLog>>(
      `${this.api}/audit`,
      { params }
    );
  }

  requestsReport(range: DateRange): Observable<Blob> {
    return this.http.get(
      `${this.api}/reports/requests`,
      {
        params: this.rangeParams(range),
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
