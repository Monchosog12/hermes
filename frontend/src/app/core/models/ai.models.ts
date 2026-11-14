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
