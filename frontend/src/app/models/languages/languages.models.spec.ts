import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LANGUAGES, languageFromFileName, languageOption, Languagesmodels } from './languages.models';

describe('languages.models', () => {
  it('expone al menos Java y Python', () => {
    const codes = LANGUAGES.map((l) => l.code);
    expect(codes).toContain('JAVA');
    expect(codes).toContain('PYTHON');
  });

  it('detecta el lenguaje por la extensión del archivo', () => {
    expect(languageFromFileName('Main.java')).toBe('JAVA');
    expect(languageFromFileName('script.py')).toBe('PYTHON');
    expect(languageFromFileName('notas.txt')).toBeNull();
  });

  it('devuelve la opción de un lenguaje', () => {
    expect(languageOption('PYTHON').extension).toBe('py');
  });
});

describe('Languagesmodels (componente)', () => {
  let fixture: ComponentFixture<Languagesmodels>;
  const el = () => fixture.nativeElement as HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Languagesmodels],
    }).compileComponents();
    fixture = TestBed.createComponent(Languagesmodels);
  });

  it('muestra una fila por lenguaje soportado', () => {
    fixture.detectChanges();
    expect(el().querySelectorAll('tbody tr').length).toBe(LANGUAGES.length);
  });
});
