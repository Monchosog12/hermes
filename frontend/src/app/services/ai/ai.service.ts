import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { errorMessage } from '../http/errors';
import { ModelInfo, ProcessRequest, ProcessResponse } from '../models/ai.models';

@Injectable({ providedIn: 'root' })
export class AiService {
  private readonly http = inject(HttpClient);
  private readonly api = `${environment.apiUrl}/ai`;

  private readonly modelsState = signal<ModelInfo[]>([]);
  private readonly modelsErrorState = signal<string | null>(null);
  private requested = false;

  readonly models = this.modelsState.asReadonly();
  readonly modelsError = this.modelsErrorState.asReadonly();

  loadModels(): void {
    if (this.requested) {
      return;
    }
    this.requested = true;
    this.modelsErrorState.set(null);
    this.http.get<ModelInfo[]>(`${this.api}/models`).subscribe({
      next: (models) => this.modelsState.set(models),
      error: (err: unknown) => {
        this.requested = false;
        this.modelsErrorState.set(errorMessage(err, 'No se pudieron cargar los modelos de IA.'));
      },
    });
  }

  process(body: ProcessRequest): Observable<ProcessResponse> {
    return this.http.post<ProcessResponse>(`${this.api}/process`, body);
  }
}
