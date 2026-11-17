import type { Compartment } from '@codemirror/state';

/** Piezas de CodeMirror que el editor carga de forma diferida. */
export interface CodeMirrorKit {
  EditorView: typeof import('codemirror').EditorView;
  basicSetup: typeof import('codemirror').basicSetup;
  EditorState: typeof import('@codemirror/state').EditorState;
  languageSlot: Compartment;
  editableSlot: Compartment;
}
