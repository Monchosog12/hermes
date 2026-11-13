import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HistoryItem, toResultView, Historymodels } from './history.models';

const item: HistoryItem = {
  id: 1,
  operation: 'TRANSLATE',
  sourceLanguage: 'JAVA',
  targetLanguage: 'PYTHON',
  modelId: 'model-a',
  modelLabel: 'Modelo A',
  originalCode: 'int a;',
  resultCode: 'a = 0',
  explanation: 'Equivalente en Python.',
  recommendations: ['Usa tipos'],
  latencyMs: 1200,
  createdAt: '2026-11-07T10:00:00Z',
};

describe('toResultView', () => {
  it('usa el lenguaje de destino cuando existe', () => {
    const view = toResultView(item);
    expect(view.resultLanguage).toBe('PYTHON');
    expect(view.response.resultCode).toBe('a = 0');
  });

  it('usa el lenguaje de origen en análisis y formateo', () => {
    expect(toResultView({ ...item, operation: 'ANALYZE', targetLanguage: null }).resultLanguage).toBe('JAVA');
  });
});

describe('Historymodels (componente)', () => {
  let fixture: ComponentFixture<Historymodels>;
  const el = () => fixture.nativeElement as HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Historymodels],
    }).compileComponents();
    fixture = TestBed.createComponent(Historymodels);
  });

  it('lista los datos que se guardan del historial', () => {
    fixture.detectChanges();
    expect(el().querySelectorAll('li').length).toBe(6);
  });
});
