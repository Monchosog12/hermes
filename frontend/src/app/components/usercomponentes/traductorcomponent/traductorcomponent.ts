import { ChangeDetectionStrategy, Component, OnInit, effect, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { AiService } from '../../../core/aiservice/aiservice.service';
import { LANGUAGES } from '../../../core/aiservice/languages';
import { MAX_CODE_CHARS } from '../../../core/aiservice/limits';
import { errorMessage } from '../../../core/http/errors';
import { LanguageCode, ResultView } from '../../../core/models/aiservice.models';

@Component({
  selector: 'app-traductorcomponent',
  standalone: false,
  templateUrl: './traductorcomponent.html',
  styleUrl: './traductorcomponent.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Traductorcomponent implements OnInit {
  protected readonly ai = inject(AiService);
  private readonly fb = inject(FormBuilder).nonNullable;

  protected readonly languages = LANGUAGES;
  protected readonly maxChars = MAX_CODE_CHARS;

  readonly code = signal('');
  protected readonly loading = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly result = signal<ResultView | null>(null);

  protected readonly form = this.fb.group({
    sourceLanguage: this.fb.control<LanguageCode>('JAVA'),
    targetLanguage: this.fb.control<LanguageCode>('PYTHON'),
    modelId: this.fb.control(''),
  });

  protected readonly sourceLanguage = signal<LanguageCode>('JAVA');

  constructor() {
    this.form.controls.sourceLanguage.valueChanges.subscribe((l) => this.sourceLanguage.set(l));

    effect(() => {
      const first = this.ai.models()[0];
      if (first && !this.form.controls.modelId.value) {
        this.form.controls.modelId.setValue(first.id);
      }
    });
  }

  ngOnInit(): void {
    this.ai.loadModels();
  }

  protected onLanguageDetected(language: LanguageCode): void {
    this.form.controls.sourceLanguage.setValue(language);
  }

  protected swap(): void {
    const { sourceLanguage, targetLanguage } = this.form.getRawValue();
    this.form.patchValue({ sourceLanguage: targetLanguage, targetLanguage: sourceLanguage });
  }

  protected submit(): void {
    const { sourceLanguage, targetLanguage, modelId } = this.form.getRawValue();
    const code = this.code();

    const problem = this.validate(code, sourceLanguage, targetLanguage, modelId);
    if (problem) {
      this.error.set(problem);
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.result.set(null);
    this.ai.process({ operation: 'TRANSLATE', sourceLanguage, targetLanguage, modelId, code }).subscribe({
      next: (response) => {
        this.result.set({
          original: code,
          sourceLanguage,
          resultLanguage: targetLanguage,
          modelLabel: this.ai.models().find((m) => m.id === modelId)?.label ?? modelId,
          response,
        });
        this.loading.set(false);
      },
      error: (err: unknown) => {
        this.error.set(errorMessage(err));
        this.loading.set(false);
      },
    });
  }

  private validate(code: string, source: LanguageCode, target: LanguageCode, modelId: string): string | null {
    if (!code.trim()) {
      return 'Escribe o carga el código que quieres traducir.';
    }
    if (code.length > MAX_CODE_CHARS) {
      return `El código supera el máximo de ${MAX_CODE_CHARS} caracteres.`;
    }
    if (source === target) {
      return 'Elige un lenguaje de destino distinto al de origen. Para analizar el mismo lenguaje usa "Mejorar código".';
    }
    if (!modelId) {
      return 'Elige un modelo de IA.';
    }
    return null;
  }
}
