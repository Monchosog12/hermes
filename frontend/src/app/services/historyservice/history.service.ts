import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, Injectable, OnInit, inject, signal } from '@angular/core';
import { errorMessage } from '../http/errors';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { HistoryItem } from '../../models/history/history.models';
import { Page } from '../../models/common/page.models';
import { LoginService } from '../login.service';

@Injectable({ providedIn: 'root' })
export class HistoryService {
  private readonly http = inject(HttpClient);
  private readonly loginServicio = inject(LoginService);
  private readonly api = `${environment.apiUrl}/history`;

  /** Arma el header "Authorization: Bearer <token>" con el token guardado al iniciar sesión. */
  private obtenerHeaders(): HttpHeaders {
    const token = this.loginServicio.obtenerToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  list(page: number, size = 10): Observable<Page<HistoryItem>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<HistoryItem>>(this.api, { params, headers: this.obtenerHeaders() });
  }

  sendByEmail(id: number): Observable<void> {
    return this.http.post<void>(`${this.api}/${id}/email`, {}, { headers: this.obtenerHeaders() });
  }
}

@Component({
  selector: 'app-historyservice',
  standalone: false,
  templateUrl: './history.service.html',
  styleUrl: './history.service.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Historyservice implements OnInit {
  private readonly history = inject(HistoryService);

  protected readonly total = signal<number | null>(null);
  protected readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.history.list(0, 1).subscribe({
      next: (page) => this.total.set(page.totalElements),
      error: (err: unknown) => this.error.set(errorMessage(err, 'No se pudo consultar tu historial.')),
    });
  }
}
