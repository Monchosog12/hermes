import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAX_CODE_CHARS, OPERATION_LABELS, Aimodels } from './ai.models';

describe('ai.models', () => {
  it('define un límite de código positivo', () => {
    expect(MAX_CODE_CHARS).toBeGreaterThan(0);
  });

  it('tiene una etiqueta para cada operación', () => {
    expect(Object.keys(OPERATION_LABELS).sort()).toEqual(['ANALYZE', 'FORMAT', 'TRANSLATE']);
  });
});

describe('Aimodels (componente)', () => {
  let fixture: ComponentFixture<Aimodels>;
  const el = () => fixture.nativeElement as HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Aimodels],
    }).compileComponents();
    fixture = TestBed.createComponent(Aimodels);
  });

  it('lista las tres operaciones y el límite de caracteres', () => {
    fixture.detectChanges();
    expect(el().querySelectorAll('li').length).toBe(3);
    expect(el().textContent).toContain('10,000');
  });
});
