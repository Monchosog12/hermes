import { Injectable, inject } from '@angular/core';
import { LoadedSource } from '../../models/files/files.models';
import { LanguageService } from '../languages/language.service';

@Injectable({ providedIn: 'root' })
export class SourceFileService {
  private readonly languageService = inject(LanguageService);

  /** Lee un archivo de código fuente y detecta su lenguaje por la extensión. */
  async read(file: File, maxChars: number): Promise<LoadedSource> {
    const language = this.languageService.fromFileName(file.name);
    if (!language) {
      throw new Error('Tipo de archivo no soportado.');
    }
    const text = await file.text();
    if (text.length > maxChars) {
      throw new Error(`El archivo supera los ${maxChars} caracteres.`);
    }
    return { text, language };
  }
}
