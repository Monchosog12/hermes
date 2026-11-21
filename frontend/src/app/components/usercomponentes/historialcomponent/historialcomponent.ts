import { Component, computed, HostListener, inject, OnInit, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { finalize } from 'rxjs';
import {
  LENGUAJES_HISTORIAL,
  TraduccionHistorial,
  TraduccionHistorialActualizacion,
} from '../../../models/history/traduccionhistorial.model';
import { HistorialService } from '../../../services/historial.service';
import { LoginService } from '../../../services/login.service';

// Bootstrap se carga globalmente desde index.html (bootstrap.bundle.min.js).
declare const bootstrap: any;

@Component({
  selector: 'app-historialcomponent',
  standalone: false,
  styleUrl: './historialcomponent.css',
  templateUrl: './historialcomponent.html',
})
export class Historialcomponent implements OnInit {
  private readonly historialService = inject(HistorialService);
  private readonly loginServicio = inject(LoginService);
  private readonly snackBar = inject(MatSnackBar);

  readonly lenguajes = LENGUAJES_HISTORIAL;

  traducciones = signal<TraduccionHistorial[]>([]);
  cargando = signal(false);
  errorCarga = signal<string | null>(null);
  logueado = signal(this.loginServicio.estaLogueado());

  busqueda = '';
  filtroLenguaje = 'todos';

  totalTraducciones = computed(() => this.traducciones().length);

  totalLenguajes = computed(() => {
    const usados = new Set<string>();
    this.traducciones().forEach((t) => {
      if (t.lenguajeOrigen) usados.add(t.lenguajeOrigen.toUpperCase());
      if (t.lenguajeDestino) usados.add(t.lenguajeDestino.toUpperCase());
    });
    return usados.size;
  });

  // La lista llega ordenada de la más reciente a la más antigua.
  ultimaTraduccion = computed(() => this.traducciones()[0]?.fecha ?? null);

  // Ventana de modificación
  traduccionEditando = signal<TraduccionHistorial | null>(null);
  formulario: TraduccionHistorialActualizacion = {
    codigoOriginal: '',
    codigoTraducido: '',
    lenguajeOrigen: '',
    lenguajeDestino: '',
  };
  guardando = false;

  // Confirmación de eliminación
  traduccionAEliminar = signal<TraduccionHistorial | null>(null);
  eliminando = false;

  ngOnInit(): void {
    this.cargarHistorial();
  }

  cargarHistorial(): void {
    this.logueado.set(this.loginServicio.estaLogueado());
    if (!this.logueado()) {
      this.traducciones.set([]);
      return;
    }

    this.cargando.set(true);
    this.errorCarga.set(null);
    this.historialService
      .listar()
      .pipe(finalize(() => this.cargando.set(false)))
      .subscribe({
        next: (lista) => this.traducciones.set(Array.isArray(lista) ? lista : []),
        error: (error: HttpErrorResponse) => {
          if (error.status === 401 || error.status === 403) {
            this.loginServicio.cerrarSesion();
            this.logueado.set(false);
            return;
          }
          this.errorCarga.set(this.obtenerMensajeError(error, 'No fue posible cargar tu historial.'));
        },
      });
  }

  get traduccionesFiltradas(): TraduccionHistorial[] {
    const termino = this.busqueda.trim().toLowerCase();

    return this.traducciones().filter((t) => {
      const coincideBusqueda =
        !termino ||
        t.codigoOriginal?.toLowerCase().includes(termino) ||
        t.codigoTraducido?.toLowerCase().includes(termino) ||
        this.etiquetaLenguaje(t.lenguajeOrigen).toLowerCase().includes(termino) ||
        this.etiquetaLenguaje(t.lenguajeDestino).toLowerCase().includes(termino);

      const coincideLenguaje =
        this.filtroLenguaje === 'todos' ||
        t.lenguajeOrigen?.toUpperCase() === this.filtroLenguaje ||
        t.lenguajeDestino?.toUpperCase() === this.filtroLenguaje;

      return coincideBusqueda && coincideLenguaje;
    });
  }

  limpiarFiltros(): void {
    this.busqueda = '';
    this.filtroLenguaje = 'todos';
  }

  abrirLogin(): void {
    const elemento = document.getElementById('modalLogin');
    if (elemento) {
      bootstrap.Modal.getOrCreateInstance(elemento).show();
    }
  }

  // ---------- Presentación ----------

  etiquetaLenguaje(codigo: string | null | undefined): string {
    if (!codigo) return 'Desconocido';
    const encontrado = this.lenguajes.find((l) => l.valor === codigo.toUpperCase());
    return encontrado ? encontrado.etiqueta : codigo;
  }

  iconoLenguaje(codigo: string | null | undefined): string {
    switch (codigo?.toUpperCase()) {
      case 'JAVA':
        return 'fa-brands fa-java';
      case 'JAVASCRIPT':
        return 'fa-brands fa-js';
      case 'PYTHON':
        return 'fa-brands fa-python';
      default:
        return 'fa-solid fa-code';
    }
  }

  claseLenguaje(codigo: string | null | undefined): string {
    switch (codigo?.toUpperCase()) {
      case 'JAVA':
        return 'lang-java';
      case 'JAVASCRIPT':
        return 'lang-js';
      case 'TYPESCRIPT':
        return 'lang-ts';
      case 'PYTHON':
        return 'lang-python';
      case 'CPP':
        return 'lang-cpp';
      case 'CSHARP':
        return 'lang-csharp';
      default:
        return 'lang-otro';
    }
  }

  formatearFecha(fecha: string | null): string {
    if (!fecha) return 'Sin fecha';
    const valor = new Date(fecha);
    if (isNaN(valor.getTime())) return fecha;
    return valor.toLocaleString('es-CO', { dateStyle: 'medium', timeStyle: 'short' });
  }

  /** Primeras líneas del código traducido para la vista previa de la tarjeta. */
  vistaPrevia(codigo: string | null | undefined, lineas = 6): string {
    if (!codigo) return '(sin código)';
    const partes = codigo.split('\n');
    const recorte = partes.slice(0, lineas).join('\n');
    return partes.length > lineas ? `${recorte}\n…` : recorte;
  }

  /** Lenguajes para el formulario, incluyendo el actual si no está en la lista. */
  opcionesLenguaje(actual: string): { valor: string; etiqueta: string }[] {
    if (!actual || this.lenguajes.some((l) => l.valor === actual)) {
      return this.lenguajes;
    }
    return [{ valor: actual, etiqueta: actual }, ...this.lenguajes];
  }

  // ---------- Modificar ----------

  abrirEdicion(t: TraduccionHistorial): void {
    this.formulario = {
      codigoOriginal: t.codigoOriginal ?? '',
      codigoTraducido: t.codigoTraducido ?? '',
      lenguajeOrigen: t.lenguajeOrigen?.toUpperCase() ?? '',
      lenguajeDestino: t.lenguajeDestino?.toUpperCase() ?? '',
    };
    this.traduccionEditando.set(t);
  }

  cerrarEdicion(): void {
    if (this.guardando) return;
    this.traduccionEditando.set(null);
  }

  guardarCambios(form: NgForm): void {
    const traduccion = this.traduccionEditando();
    if (!traduccion || this.guardando) return;

    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }

    const datos: TraduccionHistorialActualizacion = { ...this.formulario };

    this.guardando = true;
    this.historialService
      .actualizar(traduccion.id, datos)
      .pipe(finalize(() => (this.guardando = false)))
      .subscribe({
        next: () => {
          this.traducciones.update((lista) =>
            lista.map((t) => (t.id === traduccion.id ? { ...t, ...datos } : t))
          );
          this.traduccionEditando.set(null);
          this.mostrarMensaje('Traducción actualizada correctamente.', 'exito');
        },
        error: (error: HttpErrorResponse) => {
          this.mostrarMensaje(this.obtenerMensajeError(error, 'No fue posible actualizar la traducción.'), 'error');
        },
      });
  }

  // ---------- Eliminar ----------

  pedirConfirmacionEliminar(t: TraduccionHistorial): void {
    this.traduccionAEliminar.set(t);
  }

  cancelarEliminar(): void {
    if (this.eliminando) return;
    this.traduccionAEliminar.set(null);
  }

  confirmarEliminar(): void {
    const traduccion = this.traduccionAEliminar();
    if (!traduccion || this.eliminando) return;

    this.eliminando = true;
    this.historialService
      .eliminar(traduccion.id)
      .pipe(finalize(() => (this.eliminando = false)))
      .subscribe({
        next: () => {
          this.traducciones.update((lista) => lista.filter((t) => t.id !== traduccion.id));
          this.traduccionAEliminar.set(null);
          this.mostrarMensaje('Traducción eliminada correctamente.', 'exito');
        },
        error: (error: HttpErrorResponse) => {
          this.mostrarMensaje(this.obtenerMensajeError(error, 'No fue posible eliminar la traducción.'), 'error');
        },
      });
  }

  // Cierra la ventana abierta con la tecla Escape.
  @HostListener('document:keydown.escape')
  cerrarVentanas(): void {
    this.cerrarEdicion();
    this.cancelarEliminar();
  }

  // ---------- Utilidades ----------

  private obtenerMensajeError(error: HttpErrorResponse, porDefecto: string): string {
    if (error.status === 0) {
      return 'No hay conexión con el servidor.';
    }
    if (error.status === 401 || error.status === 403) {
      return 'Tu sesión expiró. Inicia sesión de nuevo.';
    }
    if (typeof error.error === 'string' && error.error.trim()) {
      return error.error;
    }
    return porDefecto;
  }

  private mostrarMensaje(mensaje: string, tipo: 'exito' | 'error'): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 4000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: tipo === 'exito' ? ['snack-exito'] : ['snack-error'],
    });
  }
}
