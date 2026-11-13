import { LanguageCode } from '../ai/ai.models';

export interface LanguageOption {
  code: LanguageCode;
  label: string;
  extension: string;
}
