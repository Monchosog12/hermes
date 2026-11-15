import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Codeeditorcomponent } from './codeeditorcomponent';

async function waitFor(check: () => boolean, ms = 4000): Promise<void> {
  const start = Date.now();
  while (!check()) {
    if (Date.now() - start > ms) {
      throw new Error('Tiempo de espera agotado');
    }
    await new Promise((r) => setTimeout(r, 25));
  }
}

describe('Codeeditorcomponent', () => {
  let fixture: ComponentFixture<Codeeditorcomponent>;
  const content = () => fixture.nativeElement.querySelector('.cm-content') as HTMLElement | null;

  beforeEach(async () => {
    await TestBed.configureTestingModule({ declarations: [Codeeditorcomponent] }).compileComponents();
    fixture = TestBed.createComponent(Codeeditorcomponent);
    fixture.componentRef.setInput('language', 'JAVA');
    fixture.componentRef.setInput('value', 'int a = 1;');
    fixture.detectChanges();
  });

  it('muestra el código recibido', async () => {
    await waitFor(() => !!content());
    expect(content()!.textContent).toContain('int a = 1;');
  });

  it('actualiza el editor cuando el texto cambia desde fuera', async () => {
    await waitFor(() => !!content());
    fixture.componentRef.setInput('value', 'x = 2');
    fixture.detectChanges();
    await waitFor(() => content()!.textContent!.includes('x = 2'));
    expect(content()!.textContent).not.toContain('int a');
  });

  it('en modo solo lectura no permite editar', async () => {
    fixture.componentRef.setInput('readOnly', true);
    fixture.detectChanges();
    await waitFor(() => !!content());
    await waitFor(() => content()!.getAttribute('contenteditable') === 'false');
    expect(content()!.getAttribute('contenteditable')).toBe('false');
  });

  it('muestra el contador de caracteres solo cuando se puede editar', async () => {
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.counter')?.textContent).toContain('10 / 10000');
    fixture.componentRef.setInput('readOnly', true);
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.counter')).toBeNull();
  });
});
