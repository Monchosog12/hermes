
import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy,
  Output,
  SimpleChanges,
  ViewChild
} from '@angular/core';

import { basicSetup } from 'codemirror';
import { EditorState, Compartment } from '@codemirror/state';
import { EditorView } from '@codemirror/view';

import { javascript } from '@codemirror/lang-javascript';
import { java } from '@codemirror/lang-java';
import { python } from '@codemirror/lang-python';

type Lenguaje = 'javascript' | 'java' | 'python';

@Component({
  selector: 'app-codeeditorcomponent',
  standalone: false,
  styleUrl: './codeeditorcomponent.css',
  templateUrl: './codeeditorcomponent.html',
})
export class Codeeditorcomponent
  implements AfterViewInit, OnChanges, OnDestroy {

  @ViewChild('editorHost')
  editorHost!: ElementRef<HTMLDivElement>;

  @Input() codigo = '';
  @Input() lenguaje: Lenguaje = 'javascript';
  @Input() soloLectura = false;

  @Output() codigoChange = new EventEmitter<string>();

  private editor?: EditorView;

  private compartimentoLenguaje = new Compartment();
  private compartimentoLectura = new Compartment();

  ngAfterViewInit(): void {
    this.crearEditor();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.editor) {
      return;
    }

    if (changes['lenguaje']) {
      this.editor.dispatch({
        effects: this.compartimentoLenguaje.reconfigure(
          this.obtenerLenguaje()
        )
      });
    }

    if (changes['soloLectura']) {
      this.editor.dispatch({
        effects: this.compartimentoLectura.reconfigure(
          this.obtenerConfiguracionLectura()
        )
      });
    }

    if (
      changes['codigo'] &&
      this.codigo !== this.editor.state.doc.toString()
    ) {
      this.editor.dispatch({
        changes: {
          from: 0,
          to: this.editor.state.doc.length,
          insert: this.codigo
        }
      });
    }
  }

  ngOnDestroy(): void {
    this.editor?.destroy();
  }

  private crearEditor(): void {
    const estado = EditorState.create({
      doc: this.codigo,
      extensions: [
        basicSetup,

        this.compartimentoLenguaje.of(
          this.obtenerLenguaje()
        ),

        this.compartimentoLectura.of(
          this.obtenerConfiguracionLectura()
        ),

        EditorView.theme({
          '&': {
            height: '100%',
            backgroundColor: '#0f1017',
            color: '#e5e7eb',
            fontSize: '14px'
          },
          '.cm-gutters': {
            backgroundColor: '#0f1017',
            color: '#858ba3',
            borderRight: '1px solid #2b3042'
          },
          '.cm-activeLine': {
            backgroundColor: '#191e30'
          },
          '.cm-activeLineGutter': {
            backgroundColor: '#20253a'
          },
          '.cm-cursor': {
            borderLeftColor: '#c4b5fd'
          },
          '&.cm-focused': {
            outline: 'none'
          }
        }, { dark: true }),

        EditorView.updateListener.of(actualizacion => {
          if (actualizacion.docChanged) {
            this.codigoChange.emit(
              actualizacion.state.doc.toString()
            );
          }
        })
      ]
    });

    this.editor = new EditorView({
      state: estado,
      parent: this.editorHost.nativeElement
    });
  }

  private obtenerLenguaje() {
    switch (this.lenguaje) {
      case 'java':
        return java();

      case 'python':
        return python();

      default:
        return javascript();
    }
  }

  private obtenerConfiguracionLectura() {
    return [
      EditorState.readOnly.of(this.soloLectura),
      EditorView.editable.of(!this.soloLectura)
    ];
  }
}
