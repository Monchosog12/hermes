import { RouterModule, provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { HistoryService, Historyservice } from './history.service';
import { environment } from '../../../environments/environment';

describe('HistoryService', () => {
  let service: HistoryService;
  let http: HttpTestingController;
  const api = `${environment.apiUrl}/history`;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(HistoryService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('pide la página indicada', () => {
    service.list(2, 5).subscribe();
    const req = http.expectOne((r) => r.url === api);
    expect(req.request.params.get('page')).toBe('2');
    expect(req.request.params.get('size')).toBe('5');
    req.flush({ content: [], totalElements: 0, totalPages: 0, number: 2, size: 5 });
  });

  it('envía el resultado por correo con POST', () => {
    service.sendByEmail(7).subscribe();
    const req = http.expectOne(`${api}/7/email`);
    expect(req.request.method).toBe('POST');
    req.flush(null);
  });
});

describe('Historyservice (componente)', () => {
  let fixture: ComponentFixture<Historyservice>;
  const el = () => fixture.nativeElement as HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Historyservice],
      imports: [RouterModule],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();
    fixture = TestBed.createComponent(Historyservice);
  });

  it('muestra el total de solicitudes del usuario', () => {
    const http = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
    http.expectOne((r) => r.url === `${environment.apiUrl}/history`)
      .flush({ content: [], totalElements: 9, totalPages: 9, number: 0, size: 1 });
    fixture.detectChanges();
    expect(el().textContent).toContain('9');
    http.verify();
  });
});
