import { Extension } from '@codemirror/state';
import { LanguageCode } from '../models/ai.models';

export interface LanguageOption {
  code: LanguageCode;
  label: string;
  extension: string; // extensión del archivo al descargar o cargar
}

export const LANGUAGES: readonly LanguageOption[] = [
  { code: 'JAVA', label: 'Java', extension: 'java' },
  { code: 'PYTHON', label: 'Python', extension: 'py' },
  { code: 'JAVASCRIPT', label: 'JavaScript', extension: 'js' },
  { code: 'TYPESCRIPT', label: 'TypeScript', extension: 'ts' },
  { code: 'CPP', label: 'C++', extension: 'cpp' },
  { code: 'CSHARP', label: 'C#', extension: 'cs' },
];

export function languageOption(code: LanguageCode): LanguageOption {
  return LANGUAGES.find((l) => l.code === code) ?? LANGUAGES[0];
}

export function languageFromFileName(fileName: string): LanguageCode | null {
  const ext = fileName.split('.').pop()?.toLowerCase();
  return LANGUAGES.find((l) => l.extension === ext)?.code ?? null;
}

export async function loadLanguageExtension(code: LanguageCode): Promise<Extension> {
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
