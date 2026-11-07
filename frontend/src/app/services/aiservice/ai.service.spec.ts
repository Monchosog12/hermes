import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../../environments/environment';
import { AiService } from './ai.service';

describe('AiService', () => {
  let service: AiService;
  let http: HttpTestingController;
  const modelsUrl = `${environment.apiUrl}/ai/models`;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(AiService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('pide la lista de modelos una sola vez', () => {
    service.loadModels();
    service.loadModels();
    http.expectOne(modelsUrl).flush([{ id: 'model-a', label: 'Modelo A' }]);
    service.loadModels();

    expect(service.models().length).toBe(1);
    expect(service.modelsError()).toBeNull();
  });

  it('informa el error y permite reintentar si falla la carga', () => {
    service.loadModels();
    http.expectOne(modelsUrl).flush({ detail: 'Servicio caído' }, { status: 502, statusText: 'Bad Gateway' });
    expect(service.modelsError()).toBe('Servicio caído');

    service.loadModels(); // reintento
    http.expectOne(modelsUrl).flush([{ id: 'model-a', label: 'Modelo A' }]);
    expect(service.modelsError()).toBeNull();
    expect(service.models().length).toBe(1);
  });

  it('envía la solicitud de procesamiento al backend', () => {
    const body = {
      operation: 'ANALYZE' as const,
      sourceLanguage: 'JAVA' as const,
      modelId: 'model-a',
      code: 'int a;',
    };
    service.process(body).subscribe();
    const req = http.expectOne(`${environment.apiUrl}/ai/process`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(body);
    req.flush({});
  });
});
