import { ChangeDetectionStrategy, Component } from '@angular/core';
import { LanguageCode, Operation, ResultView } from '../ai/ai.models';

export interface HistoryItem {
  id: number;
  operation: Operation;
  sourceLanguage: LanguageCode;
  targetLanguage: LanguageCode | null; // null en ANALYZE y FORMAT
  modelId: string;
  modelLabel: string;
  originalCode: string;
  resultCode: string;
  explanation: string;
  recommendations: string[];
  latencyMs: number;
  createdAt: string; // ISO-8601
}

export function toResultView(item: HistoryItem): ResultView {
  return {
    original: item.originalCode,
    sourceLanguage: item.sourceLanguage,
    resultLanguage: item.targetLanguage ?? item.sourceLanguage,
    modelLabel: item.modelLabel,
    response: {
      modelId: item.modelId,
      operation: item.operation,
      resultCode: item.resultCode,
      explanation: item.explanation,
      recommendations: item.recommendations,
      latencyMs: item.latencyMs,
    },
  };
}

export const HISTORY_FIELDS: readonly string[] = [
  'Fecha y hora de la solicitud',
  'Lenguaje de origen y de destino',
  'Modelo de IA utilizado',
  'Tipo de operación',
  'Código original y resultado',
  'Explicación y recomendaciones',
];

@Component({
  selector: 'app-historymodels',
  standalone: false,
  templateUrl: './history.models.html',
  styleUrl: './history.models.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Historymodels {
  protected readonly fields = HISTORY_FIELDS;
}
