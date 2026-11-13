import { Authmodels } from './auth.models';
import { ComponentFixture, TestBed } from '@angular/core/testing';

describe('Authmodels (componente)', () => {
  let fixture: ComponentFixture<Authmodels>;
  const el = () => fixture.nativeElement as HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Authmodels],
    }).compileComponents();
    fixture = TestBed.createComponent(Authmodels);
  });

  it('explica los roles y la regla de contraseña', () => {
    fixture.detectChanges();
    expect(el().textContent).toContain('Administrador');
    expect(el().textContent).toContain('Mínimo 10 caracteres');
  });
});
