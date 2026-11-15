import { ChangeDetectionStrategy, Component, OnInit, effect, inject, signal } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { AiService } from '../../../core/aiservice/aiservice.service';
import { LANGUAGES } from '../../../core/aiservice/languages';
import { MAX_CODE_CHARS } from '../../../core/aiservice/limits';
import { errorMessage } from '../../../core/http/errors';
import { LanguageCode, Operation, ResultView } from '../../../core/models/aiservice.models';

type ImproveOperation = Extract<Operation, 'ANALYZE' | 'FORMAT'>;

@Component({
  selector: 'app-mejoracodigocomponent',
  standalone: false,
  templateUrl: './mejoracodigocomponent.html',
  styleUrl: './mejoracodigocomponent.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Mejoracodigocomponent implements OnInit {
  protected readonly ai = inject(AiService);
  private readonly fb = inject(FormBuilder).nonNullable;

  protected readonly languages = LANGUAGES;
  protected readonly maxChars = MAX_CODE_CHARS;

  readonly code = signal('');
  protected readonly loading = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly result = signal<ResultView | null>(null);

  protected readonly form = this.fb.group({
    language: this.fb.control<LanguageCode>('JAVA'),
    operation: this.fb.control<ImproveOperation>('ANALYZE'),
    modelId: this.fb.control(''),
  });

  protected readonly language = signal<LanguageCode>('JAVA');

  constructor() {
    this.form.controls.language.valueChanges.subscribe((l) => this.language.set(l));

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
    this.form.controls.language.setValue(language);
  }

  protected submit(): void {
    const { language, operation, modelId } = this.form.getRawValue();
    const code = this.code();

    if (!code.trim()) {
      this.error.set('Escribe o carga el código que quieres mejorar.');
      return;
    }
    if (code.length > MAX_CODE_CHARS) {
      this.error.set(`El código supera el máximo de ${MAX_CODE_CHARS} caracteres.`);
      return;
    }
    if (!modelId) {
      this.error.set('Elige un modelo de IA.');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.result.set(null);
    this.ai.process({ operation, sourceLanguage: language, modelId, code }).subscribe({
      next: (response) => {
        this.result.set({
          original: code,
          sourceLanguage: language,
          resultLanguage: language,
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
}
