import { ChangeDetectionStrategy, Component } from '@angular/core';
export type LanguageCode = 'JAVA' | 'PYTHON' | 'JAVASCRIPT' | 'TYPESCRIPT' | 'CPP' | 'CSHARP';
export type Operation = 'TRANSLATE' | 'ANALYZE' | 'FORMAT';

export interface ModelInfo {
  id: string;
  label: string;
}

export interface ProcessRequest {
  operation: Operation;
  sourceLanguage: LanguageCode;
  targetLanguage?: LanguageCode; // obligatorio solo en TRANSLATE
  modelId: string;
  code: string;
}

export interface ProcessResponse {
  modelId: string;
  operation: Operation;
  resultCode: string;
  explanation: string;
  recommendations: string[];
  latencyMs: number;
}

export interface ResultView {
  original: string;
  sourceLanguage: LanguageCode;
  resultLanguage: LanguageCode;
  modelLabel: string;
  response: ProcessResponse;
}
export const MAX_CODE_CHARS = 10_000;

export const OPERATION_LABELS: Readonly<Record<Operation, string>> = {
  TRANSLATE: 'Traducción',
  ANALYZE: 'Análisis',
  FORMAT: 'Formateo',
};

const OPERATION_HELP: Readonly<Record<Operation, string>> = {
  TRANSLATE: 'Convierte tu código a otro lenguaje conservando su lógica.',
  ANALYZE: 'Revisa errores y buenas prácticas sin cambiar el lenguaje.',
  FORMAT: 'Ordena y mejora la legibilidad del código.',
};

@Component({
  selector: 'app-aimodels',
  standalone: false,
  templateUrl: './ai.models.html',
  styleUrl: './ai.models.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Aimodels {
  protected readonly operations = (Object.keys(OPERATION_LABELS) as Operation[]).map((code) => ({
    code,
    label: OPERATION_LABELS[code],
    help: OPERATION_HELP[code],
  }));
  protected readonly maxChars = MAX_CODE_CHARS;
}
