import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ResultView } from '../../../core/models/aiservice.models';
import { Codeeditorcomponent } from '../codeeditorcomponent/codeeditorcomponent';
import { Resultcomponent } from './resultcomponent';

const view: ResultView = {
  original: 'System.out.println("Hola");',
  sourceLanguage: 'JAVA',
  resultLanguage: 'PYTHON',
  modelLabel: 'Modelo A',
  response: {
    modelId: 'model-a',
    operation: 'TRANSLATE',
    resultCode: 'print("Hola")',
    explanation: 'println equivale a print.',
    recommendations: ['Usa f-strings', 'Agrega pruebas'],
    latencyMs: 1840,
  },
};

describe('Resultcomponent', () => {
  let fixture: ComponentFixture<Resultcomponent>;
  const text = () => (fixture.nativeElement as HTMLElement).textContent ?? '';

  beforeEach(async () => {
    await TestBed.configureTestingModule({ declarations: [Resultcomponent, Codeeditorcomponent] }).compileComponents();
    fixture = TestBed.createComponent(Resultcomponent);
    fixture.componentRef.setInput('view', view);
    fixture.detectChanges();
  });

  it('muestra modelo, tiempo, explicación y sugerencias', () => {
    expect(text()).toContain('Modelo: Modelo A');
    expect(text()).toContain('1.8 s');
    expect(text()).toContain('println equivale a print.');
    expect(fixture.nativeElement.querySelectorAll('li').length).toBe(2);
  });

  it('muestra la explicación como texto y no interpreta HTML de la IA', () => {
    fixture.componentRef.setInput('view', {
      ...view,
      response: { ...view.response, explanation: '<img src=x onerror=alert(1)>' },
    });
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.explanation img')).toBeNull();
    expect(text()).toContain('<img src=x onerror=alert(1)>');
  });

  it('avisa si el navegador bloquea el portapapeles', async () => {
    spyOn(navigator.clipboard, 'writeText').and.rejectWith(new Error('bloqueado'));
    const copyButton = fixture.nativeElement.querySelector('.actions button') as HTMLButtonElement;
    copyButton.click();
    await fixture.whenStable();
    fixture.detectChanges();
    expect(text()).toContain('No se pudo copiar');
  });
});
