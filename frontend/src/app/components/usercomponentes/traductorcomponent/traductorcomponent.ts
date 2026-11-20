
import { Component, HostListener, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { finalize } from 'rxjs';
import { LanguageCode } from '../../../models/ai/ai.models';
import { TraduccionModel } from '../../../models/traduccion.model';
import { TraduccionService } from '../../../services/traduccion.service';
import { LoginService } from '../../../services/login.service';
import { ModeloIA } from '../../../models/ai/modeloia.model';

// Bootstrap se carga globalmente desde index.html (bootstrap.bundle.min.js).
declare const bootstrap: any;

@Component({
  selector: 'app-traductorcomponent',
  standalone: false,
  styleUrl: './traductorcomponent.css',
  templateUrl: './traductorcomponent.html',
})
export class Traductorcomponent {

  private readonly servicio = inject(TraduccionService);
  private readonly loginServicio = inject(LoginService);
  private readonly snackBar = inject(MatSnackBar);

  lenguajeOrigen: LanguageCode = 'JAVASCRIPT';
  lenguajeDestino: LanguageCode = 'PYTHON';

  codigoOrigen = '';
  codigoDestino = '';

  mejoras: string[] = [];
  cargando = false;

  readonly modelos: ModeloIA[] = [
    { endpoint: 'cohere', nombre: 'Cohere North Mini Code', descripcion: 'Rápido y ligero' },
    { endpoint: 'nemotron', nombre: 'NVIDIA Nemotron 3 Ultra', descripcion: 'Mayor capacidad de razonamiento' },
    { endpoint: 'particle', nombre: 'Particle Coder', descripcion: 'Especializado en código' },
  ];

  modeloSeleccionado: ModeloIA = this.modelos[0];
  menuModelosAbierto = false;

  seleccionarModelo(event: MouseEvent): void {
    event.stopPropagation();
    this.menuModelosAbierto = !this.menuModelosAbierto;
  }

  elegirModelo(modelo: ModeloIA): void {
    this.modeloSeleccionado = modelo;
    this.menuModelosAbierto = false;
  }

  // Cierra la lista al hacer clic fuera de ella o al presionar Escape.
  @HostListener('document:click')
  @HostListener('document:keydown.escape')
  cerrarMenuModelos(): void {
    this.menuModelosAbierto = false;
  }

  traducir(): void {
    if (this.cargando) return;

    if (!this.codigoOrigen.trim()) {
      this.mostrarMensaje('Escribe el código que quieres traducir.', 'error');
      return;
    }

    if (!this.loginServicio.estaLogueado()) {
      this.mostrarMensaje('Inicia sesión para traducir.', 'error');
      this.abrirModalLogin();
      return;
    }

    const datos: TraduccionModel = {
      codigo: this.codigoOrigen,
      lenguajeOrigen: this.lenguajeOrigen,
      lenguajeDestino: this.lenguajeDestino,
    };
    this.cargando = true;

    this.servicio
      .traducir(datos, this.modeloSeleccionado.endpoint)
      .pipe(finalize(() => (this.cargando = false)))
      .subscribe({
        next: (respuesta) => {
          this.codigoDestino = respuesta.codigoTraducido;
          this.mejoras = respuesta.mejoras ?? [];
          this.mostrarMensaje('Traducción lista.', 'exito');
        },
        error: (error: HttpErrorResponse) => {
          this.mostrarMensaje(this.obtenerMensajeError(error), 'error');
        },
      });
  }

  intercambiarLenguajes(): void {
    const lenguajeTemporal = this.lenguajeOrigen;

    this.lenguajeOrigen = this.lenguajeDestino;
    this.lenguajeDestino = lenguajeTemporal;

    const codigoTemporal = this.codigoOrigen;

    this.codigoOrigen = this.codigoDestino;
    this.codigoDestino = codigoTemporal;
    this.mejoras = [];
  }

  limpiar(): void {
    this.codigoOrigen = '';
    this.codigoDestino = '';
    this.mejoras = [];
  }

  copiarResultado(): void {
    if (!this.codigoDestino.trim()) {
      return;
    }

    navigator.clipboard.writeText(this.codigoDestino).catch(error => {
      console.error('No se pudo copiar el código:', error);
    });
  }

  private abrirModalLogin(): void {
    const elemento = document.getElementById('modalLogin');
    if (elemento) {
      bootstrap.Modal.getOrCreateInstance(elemento).show();
    }
  }

  private mostrarMensaje(mensaje: string, tipo: 'exito' | 'error'): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 4000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: tipo === 'exito' ? ['snack-exito'] : ['snack-error'],
    });
  }

  private obtenerMensajeError(error: HttpErrorResponse): string {
    switch (error.status) {
      case 0:
        return 'No hay conexión con el servidor.';
      case 401:
      case 403:
        // El token no es válido o expiró: se cierra la sesión local para que vuelva a entrar.
        this.loginServicio.cerrarSesion();
        this.abrirModalLogin();
        return 'Tu sesión expiró. Inicia sesión de nuevo.';
      case 502:
        return 'El servicio de IA no respondió correctamente. Intenta de nuevo.';
      case 503:
        return 'El modelo de IA está saturado. Intenta de nuevo en unos minutos.';
      default:
        return 'No fue posible traducir el código. Intenta de nuevo.';
    }
  }
}
