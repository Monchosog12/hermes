import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, Injectable, OnInit, inject, signal } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { errorMessage } from '../http/errors';
import { ModelInfo, ProcessRequest, ProcessResponse } from '../../models/ai/ai.models';
import { LoginService } from '../login.service';

@Injectable({ providedIn: 'root' })
export class AiService {
  private readonly http = inject(HttpClient);
  private readonly loginServicio = inject(LoginService);
  private readonly api = `${environment.apiUrl}/ai`;
  private readonly modelsState = signal<ModelInfo[]>([]);
  private readonly modelsErrorState = signal<string | null>(null);
  private requested = false;

  readonly models = this.modelsState.asReadonly();
  readonly modelsError = this.modelsErrorState.asReadonly();

  /** Arma el header "Authorization: Bearer <token>" con el token guardado al iniciar sesión. */
  private obtenerHeaders(): HttpHeaders {
    const token = this.loginServicio.obtenerToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  loadModels(): void {
    if (this.requested) {
      return;
    }
    this.requested = true;
    this.modelsErrorState.set(null);
    this.http.get<ModelInfo[]>(`${this.api}/models`, { headers: this.obtenerHeaders() }).subscribe({
      next: (models) => this.modelsState.set(models),
      error: (err: unknown) => {
        this.requested = false;
        this.modelsErrorState.set(errorMessage(err, 'No se pudieron cargar los modelos de IA.'));
      },
    });
  }

  process(body: ProcessRequest): Observable<ProcessResponse> {
    return this.http.post<ProcessResponse>(`${this.api}/process`, body, { headers: this.obtenerHeaders() });
  }
}

@Component({
  selector: 'app-aiservice',
  standalone: false,
  templateUrl: './ai.service.html',
  styleUrl: './ai.service.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Aiservice implements OnInit {
  protected readonly ai = inject(AiService);

  ngOnInit(): void {
    this.ai.loadModels();
  }
}
