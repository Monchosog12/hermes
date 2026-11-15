import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideRouter, RouterModule } from '@angular/router';
import { environment } from '../../../environments/environment';
import { AuthResponse } from '../../models/auth/auth.models';
import { AuthService, Authservice } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let http: HttpTestingController;

  const response: AuthResponse = {
    accessToken: 'jwt-de-prueba',
    tokenType: 'Bearer',
    expiresIn: 900,
    user: { id: 1, fullName: 'Ana', email: 'ana@correo.com', role: 'CLIENT', status: 'ACTIVE' },
  };

  beforeEach(() => {
    sessionStorage.clear();
    TestBed.configureTestingModule({
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(AuthService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('guarda la sesión al iniciar sesión', () => {
    service.login({ email: 'ana@correo.com', password: 'Str0ng#Pass2026' }).subscribe();
    http.expectOne(`${environment.apiUrl}/auth/login`).flush(response);

    expect(service.isAuthenticated()).toBeTrue();
    expect(service.token()).toBe('jwt-de-prueba');
    expect(service.isAdmin()).toBeFalse();
  });

  it('limpia la sesión al cerrar sesión', () => {
    service.login({ email: 'ana@correo.com', password: 'x' }).subscribe();
    http.expectOne(`${environment.apiUrl}/auth/login`).flush(response);

    service.logout();

    expect(service.isAuthenticated()).toBeFalse();
    expect(sessionStorage.getItem('codeforge.session')).toBeNull();
  });

  it('envía el token al confirmar el correo', () => {
    service.confirmEmail('abc').subscribe();
    const req = http.expectOne(`${environment.apiUrl}/auth/confirm`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ token: 'abc' });
    req.flush(null);
  });
});

describe('Authservice (componente)', () => {
  let fixture: ComponentFixture<Authservice>;
  const el = () => fixture.nativeElement as HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Authservice],
      imports: [RouterModule],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();
    sessionStorage.setItem(
      'codeforge.session',
      JSON.stringify({
        accessToken: 't',
        expiresAt: Date.now() + 60_000,
        user: { id: 1, fullName: 'Ana Pérez', email: 'ana@correo.com', role: 'CLIENT', status: 'ACTIVE' },
      }),
    );
    fixture = TestBed.createComponent(Authservice);
  });

  afterEach(() => sessionStorage.clear());

  it('muestra el nombre y el rol de la sesión y permite cerrar sesión', () => {
    fixture.detectChanges();
    expect(el().textContent).toContain('Ana Pérez · Cliente');
    expect(el().querySelector('button')?.textContent).toContain('Cerrar sesión');
  });
});
