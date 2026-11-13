import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { environment } from '../../../../environments/environment';
import { Codeeditorcomponent } from '../codeeditorcomponent/codeeditorcomponent';
import { Resultcomponent } from '../resultcomponent/resultcomponent';
import { Traductorcomponent } from './traductorcomponent';

describe('Traductorcomponent', () => {
  let fixture: ComponentFixture<Traductorcomponent>;
  let http: HttpTestingController;
  const el = () => fixture.nativeElement as HTMLElement;
  const submit = () => el().querySelector('form')!.dispatchEvent(new Event('submit'));
  const alertText = () => el().querySelector('[role="alert"]')?.textContent ?? '';

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Traductorcomponent, Codeeditorcomponent, Resultcomponent],
      imports: [ReactiveFormsModule],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();
    fixture = TestBed.createComponent(Traductorcomponent);
    http = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
    http.expectOne(`${environment.apiUrl}/ai/models`).flush([
      { id: 'model-a', label: 'Modelo A' },
      { id: 'model-b', label: 'Modelo B' },
    ]);
    fixture.detectChanges();
    fixture.detectChanges();
  });

  afterEach(() => http.verify());

  it('preselecciona el primer modelo disponible', () => {
    expect((el().querySelector('#modelId') as HTMLSelectElement).value).toBe('model-a');
  });

  it('pide escribir código antes de traducir y no llama al backend', () => {
    submit();
    fixture.detectChanges();
    expect(alertText()).toContain('Escribe o carga');
    http.expectNone(`${environment.apiUrl}/ai/process`);
  });

  it('no permite traducir al mismo lenguaje', () => {
    fixture.componentInstance.code.set('int a;');
    const target = el().querySelector('#targetLanguage') as HTMLSelectElement;
    target.value = 'JAVA';
    target.dispatchEvent(new Event('change'));
    submit();
    fixture.detectChanges();
    expect(alertText()).toContain('lenguaje de destino distinto');
    http.expectNone(`${environment.apiUrl}/ai/process`);
  });
 
  it('envía TRANSLATE con los datos del formulario y muestra el resultado', () => {
    fixture.componentInstance.code.set('System.out.println("Hola");');
    submit();
    const req = http.expectOne(`${environment.apiUrl}/ai/process`);
    expect(req.request.body).toEqual({
      operation: 'TRANSLATE',
      sourceLanguage: 'JAVA',
      targetLanguage: 'PYTHON',
      modelId: 'model-a',
      code: 'System.out.println("Hola");',
    });
    req.flush({
      modelId: 'model-a',
      operation: 'TRANSLATE',
      resultCode: 'print("Hola")',
      explanation: 'println equivale a print.',
      recommendations: [],
      latencyMs: 900,
    });
    fixture.detectChanges();
    expect(el().querySelector('app-resultcomponent')).not.toBeNull();
    expect(el().textContent).toContain('println equivale a print.');
  });

  it('muestra el mensaje del servidor si la IA falla', () => {
    fixture.componentInstance.code.set('int a;');
    submit();
    http
      .expectOne(`${environment.apiUrl}/ai/process`)
      .flush({ detail: 'El servicio de IA no está disponible. Intenta de nuevo.' }, { status: 502, statusText: 'Bad Gateway' });
    fixture.detectChanges();
    expect(alertText()).toContain('no está disponible');
    expect(el().querySelector('app-resultcomponent')).toBeNull();
  });
});
