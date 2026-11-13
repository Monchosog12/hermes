
import { Component } from '@angular/core';

type Lenguaje = 'javascript' | 'java' | 'python';

@Component({
  selector: 'app-traductorcomponent',
  standalone: false,
  styleUrl: './traductorcomponent.css',
  templateUrl: './traductorcomponent.html',
})
export class Traductorcomponent {

  lenguajeOrigen: Lenguaje = 'javascript';
  lenguajeDestino: Lenguaje = 'python';

  codigoOrigen = '';
  codigoDestino = '';

  traducir(): void {
    // La traducción real se implementará posteriormente.
    console.log('Traducción pendiente de implementar');
  }

  intercambiarLenguajes(): void {
    const lenguajeTemporal = this.lenguajeOrigen;

    this.lenguajeOrigen = this.lenguajeDestino;
    this.lenguajeDestino = lenguajeTemporal;

    const codigoTemporal = this.codigoOrigen;

    this.codigoOrigen = this.codigoDestino;
    this.codigoDestino = codigoTemporal;
  }

  limpiar(): void {
    this.codigoOrigen = '';
    this.codigoDestino = '';
  }

  copiarResultado(): void {
    if (!this.codigoDestino.trim()) {
      return;
    }

    navigator.clipboard.writeText(this.codigoDestino).catch(error => {
      console.error('No se pudo copiar el código:', error);
    });
  }
}
