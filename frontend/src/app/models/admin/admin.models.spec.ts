import { ComponentFixture, TestBed } from '@angular/core/testing';
import { lastDays, Adminmodels } from './admin.models';

describe('lastDays', () => {
  it('devuelve el rango de los últimos días en formato yyyy-MM-dd', () => {
    const range = lastDays(30, new Date('2026-11-07T12:00:00Z'));
    expect(range).toEqual({ from: '2026-10-08', to: '2026-11-07' });
  });
});

describe('Adminmodels (componente)', () => {
  let fixture: ComponentFixture<Adminmodels>;
  const el = () => fixture.nativeElement as HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Adminmodels],
    }).compileComponents();
    fixture = TestBed.createComponent(Adminmodels);
  });

  it('muestra la leyenda de los tres estados de usuario', () => {
    fixture.detectChanges();
    expect(el().querySelectorAll('li').length).toBe(3);
    expect(el().textContent).toContain('BLOCKED');
  });
});
