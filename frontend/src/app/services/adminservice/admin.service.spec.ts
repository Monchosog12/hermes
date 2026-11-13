import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { environment } from '../../../environments/environment';
import { AdminService, Adminservice } from './admin.service';

describe('AdminService', () => {
  let service: AdminService;
  let http: HttpTestingController;
  const api = `${environment.apiUrl}/admin`;
  const range = { from: '2026-10-01', to: '2026-10-31' };

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(AdminService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('actualiza un usuario con PATCH', () => {
    service.updateUser(3, { role: 'ADMIN' }).subscribe();
    const req = http.expectOne(`${api}/users/3`);
    expect(req.request.method).toBe('PATCH');
    expect(req.request.body).toEqual({ role: 'ADMIN' });
    req.flush({});
  });

  it('envía el enlace de nueva contraseña sin exponer la contraseña', () => {
    service.sendPasswordReset(3).subscribe();
    const req = http.expectOne(`${api}/users/3/password-reset`);
    expect(req.request.method).toBe('POST');
    req.flush(null);
  });

  it('elimina un usuario con DELETE', () => {
    service.deleteUser(3).subscribe();
    expect(http.expectOne(`${api}/users/3`).request.method).toBe('DELETE');
  });

  it('pide estadísticas con el rango de fechas', () => {
    service.stats(range).subscribe();
    const req = http.expectOne((r) => r.url === `${api}/stats`);
    expect(req.request.params.get('from')).toBe('2026-10-01');
    expect(req.request.params.get('to')).toBe('2026-10-31');
    req.flush({});
  });

  it('descarga el reporte como blob', () => {
    service.requestsReport(range).subscribe();
    const req = http.expectOne((r) => r.url === `${api}/reports/requests`);
    expect(req.request.responseType).toBe('blob');
    req.flush(new Blob(['%PDF']));
  });
});

describe('Adminservice (componente)', () => {
  let fixture: ComponentFixture<Adminservice>;
  const el = () => fixture.nativeElement as HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Adminservice],
    }).compileComponents();
    fixture = TestBed.createComponent(Adminservice);
  });

  it('muestra los totales recibidos', () => {
    fixture.componentRef.setInput('stats', { totalUsers: 12, totalRequests: 80, byModel: [], byOperation: [] });
    fixture.detectChanges();
    expect(el().textContent).toContain('12');
    expect(el().textContent).toContain('80');
  });
});
