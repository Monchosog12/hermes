import { Injectable } from '@angular/core';
import type { Extension } from '@codemirror/state';
import { LanguageCode } from '../../models/ai/ai.models';
import { LanguageOption } from '../../models/languages/languages.models';

@Injectable({ providedIn: 'root' })
export class LanguageService {
  readonly languages: readonly LanguageOption[] = [
    { code: 'JAVA', label: 'Java', extension: 'java' },
    { code: 'PYTHON', label: 'Python', extension: 'py' },
    { code: 'JAVASCRIPT', label: 'JavaScript', extension: 'js' },
    { code: 'TYPESCRIPT', label: 'TypeScript', extension: 'ts' },
    { code: 'CPP', label: 'C++', extension: 'cpp' },
    { code: 'CSHARP', label: 'C#', extension: 'cs' },
  ];

  /** Extensiones aceptadas por un <input type="file">, por ejemplo ".java,.py". */
  readonly acceptedExtensions: string = this.languages.map((l) => `.${l.extension}`).join(',');

  option(code: LanguageCode): LanguageOption {
    return this.languages.find((l) => l.code === code) ?? this.languages[0];
  }

  fromFileName(fileName: string): LanguageCode | null {
    const ext = fileName.split('.').pop()?.toLowerCase();
    return this.languages.find((l) => l.extension === ext)?.code ?? null;
  }

  /** Carga bajo demanda el resaltado de sintaxis de CodeMirror para el lenguaje. */
  async loadExtension(code: LanguageCode): Promise<Extension> {
    switch (code) {
      case 'JAVA':
        return (await import('@codemirror/lang-java')).java();
      case 'PYTHON':
        return (await import('@codemirror/lang-python')).python();
      case 'JAVASCRIPT':
        return (await import('@codemirror/lang-javascript')).javascript();
      case 'TYPESCRIPT':
        return (await import('@codemirror/lang-javascript')).javascript({ typescript: true });
      case 'CPP':
        return (await import('@codemirror/lang-cpp')).cpp();
      case 'CSHARP': {
        const [{ StreamLanguage }, { csharp }] = await Promise.all([
          import('@codemirror/language'),
          import('@codemirror/legacy-modes/mode/clike'),
        ]);
        return StreamLanguage.define(csharp);
      }
    }
  }
}
