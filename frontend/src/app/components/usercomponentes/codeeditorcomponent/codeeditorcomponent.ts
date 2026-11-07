import type { Compartment, Extension } from '@codemirror/state';
import type { EditorView } from '@codemirror/view';
import { LANGUAGES, loadLanguageExtension } from '../../../models/languages';
import { LanguageCode } from '../../../models/aiservice.models.ts';
import { LoadedSource, readSourceFile } from '../../../core/files/source-file';

const MONO = "ui-monospace, SFMono-Regular, Menlo, Consolas, 'Liberation Mono', monospace";

interface CodeMirrorKit {
  EditorView: typeof import('codemirror').EditorView;
  basicSetup: typeof import('codemirror').basicSetup;
  EditorState: typeof import('@codemirror/state').EditorState;
  languageSlot: Compartment;
  editableSlot: Compartment;
}


@Component({
  selector: 'app-codeeditorcomponent',
  standalone: false,
  templateUrl: './codeeditorcomponent.html',
  styleUrl: './codeeditorcomponent.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Codeeditorcomponent implements OnDestroy {

  readonly value = model('');
  readonly language = input.required<LanguageCode>();
  readonly label = input('Código');
  readonly readOnly = input(false);
  readonly allowUpload = input(false);
  readonly maxChars = input(10_000);

  readonly languageDetected = output<LanguageCode>();

  protected readonly uploadError = signal<string | null>(null);
  protected readonly accept = LANGUAGES.map((l) => `.${l.extension}`).join(',');

  private readonly host = viewChild.required<ElementRef<HTMLDivElement>>('host');
  private kit?: CodeMirrorKit;
  private view?: EditorView;
  private destroyed = false;

  constructor() {
    afterNextRender(() => void this.createView());

    effect(() => {
      const text = this.value();
      const view = this.view;
      if (view && view.state.doc.toString() !== text) {
        view.dispatch({ changes: { from: 0, to: view.state.doc.length, insert: text } });
      }
    });

    effect(() => {
      const code = this.language();
      if (this.view) {
        void this.applyLanguage(code);
      }
    });

    effect(() => {
      const readOnly = this.readOnly();
      if (this.view && this.kit) {
        this.view.dispatch({ effects: this.kit.editableSlot.reconfigure(this.editable(readOnly)) });
      }
    });
  }

  ngOnDestroy(): void {
    this.destroyed = true;
    this.view?.destroy();
  }

  protected async onFile(input: HTMLInputElement): Promise<void> {
    const file = input.files?.[0];
    input.value = '';
    if (!file) {
      return;
    }
    try {
      const loaded: LoadedSource = await readSourceFile(file, this.maxChars());
      this.uploadError.set(null);
      this.value.set(loaded.text);
      this.languageDetected.emit(loaded.language);
    } catch (err) {
      this.uploadError.set(err instanceof Error ? err.message : 'No se pudo leer el archivo.');
    }
  }

  private async createView(): Promise<void> {
    const dark = window.matchMedia?.('(prefers-color-scheme: dark)').matches ?? false;
    const [{ EditorView, basicSetup }, { EditorState, Compartment }, darkTheme] = await Promise.all([
      import('codemirror'),
      import('@codemirror/state'),
      dark ? import('@codemirror/theme-one-dark').then((m) => [m.oneDark]) : Promise.resolve([]),
    ]);
    if (this.destroyed) {
      return;
    }
    this.kit = { EditorView, basicSetup, EditorState, languageSlot: new Compartment(), editableSlot: new Compartment() };

    this.view = new EditorView({
      parent: this.host().nativeElement,
      state: EditorState.create({
        doc: this.value(),
        extensions: [
          basicSetup,
          EditorView.lineWrapping,
          EditorView.contentAttributes.of({ 'aria-label': this.label() }),
          ...darkTheme,
          EditorView.theme({
            '&': { maxHeight: '60vh', minHeight: '220px' },
            '.cm-scroller': { overflow: 'auto', fontFamily: MONO, fontSize: '0.9rem' },
          }),
          this.kit.languageSlot.of([]),
          this.kit.editableSlot.of(this.editable(this.readOnly())),
          EditorView.updateListener.of((update) => {
            if (update.docChanged) {
              this.value.set(update.state.doc.toString());
            }
          }),
        ],
      }),
    });
    await this.applyLanguage(this.language());
  }

  private editable(readOnly: boolean): Extension {
    const { EditorState, EditorView } = this.kit!;
    return [EditorState.readOnly.of(readOnly), EditorView.editable.of(!readOnly)];
  }

  private async applyLanguage(code: LanguageCode): Promise<void> {
    const extension = await loadLanguageExtension(code);
    if (this.destroyed || !this.view || !this.kit || this.language() !== code) {
      return;
    }
    this.view.dispatch({ effects: this.kit.languageSlot.reconfigure(extension) });
  }
}
