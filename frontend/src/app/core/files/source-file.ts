import { languageFromFileName } from '../ai/languages';
import { LanguageCode } from '../models/ai.models';

export interface LoadedSource {
  text: string;
  language: LanguageCode;
}

export async function readSourceFile(file: File, maxChars: number): Promise<LoadedSource> {
  const language = languageFromFileName(file.name);
  if (!language) {
    throw new Error('Tipo de archivo no soportado. Usa .java, .py, .js, .ts, .cpp o .cs.');
  }
  if (file.size > maxChars * 4) {
    throw new Error(`El archivo es demasiado grande (máximo ${maxChars} caracteres).`);
  }
  const text = await file.text();
  if (text.length > maxChars) {
    throw new Error(`El archivo supera los ${maxChars} caracteres permitidos.`);
  }
  return { text, language };
}

export function downloadTextFile(fileName: string, content: string): void {
  const url = URL.createObjectURL(new Blob([content], { type: 'text/plain;charset=utf-8' }));
  const link = document.createElement('a');
  link.href = url;
  link.download = fileName;
  link.click();
  URL.revokeObjectURL(url);
}
