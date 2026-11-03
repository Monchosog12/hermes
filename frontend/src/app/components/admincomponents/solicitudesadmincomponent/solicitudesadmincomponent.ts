import { Component } from '@angular/core';
type EstadoSolicitud = 'Pendiente' | 'En proceso' | 'Atendida';

interface Solicitud {
  id: number;
  usuario: string;
  correo: string;
  tipo: string;
  fecha: string;
  estado: EstadoSolicitud;
  descripcion: string;
}

@Component({
  selector: 'app-solicitudesadmincomponent',
  standalone: false,
  styleUrl: './solicitudesadmincomponent.css',
  templateUrl: './solicitudesadmincomponent.html',
})
export class Solicitudesadmincomponent {busqueda = '';

  filtroEstado: 'Todas' | EstadoSolicitud = 'Todas';

  solicitudes: Solicitud[] = [
    {
      id: 1001,
      usuario: 'Laura Martínez',
      correo: 'laura.martinez@email.com',
      tipo: 'Mejora de código',
      fecha: '02/11/2026',
      estado: 'Pendiente',
      descripcion: 'Solicitud para mejorar la legibilidad y estructura de un código Java.'
    },
    {
      id: 1002,
      usuario: 'Carlos Rodríguez',
      correo: 'carlos.rodriguez@email.com',
      tipo: 'Traducción de código',
      fecha: '01/11/2026',
      estado: 'En proceso',
      descripcion: 'Solicitud para traducir un programa de Python a Java.'
    },
    {
      id: 1003,
      usuario: 'Mariana López',
      correo: 'mariana.lopez@email.com',
      tipo: 'Mejora de código',
      fecha: '30/10/2026',
      estado: 'Atendida',
      descripcion: 'Revisión de buenas prácticas y optimización de código JavaScript.'
    },
    {
      id: 1004,
      usuario: 'Andrés Gómez',
      correo: 'andres.gomez@email.com',
      tipo: 'Traducción de código',
      fecha: '29/10/2026',
      estado: 'Pendiente',
      descripcion: 'Traducción de un algoritmo de Java a Python.'
    },
    {
      id: 1005,
      usuario: 'Sofía Ramírez',
      correo: 'sofia.ramirez@email.com',
      tipo: 'Mejora de código',
      fecha: '28/10/2026',
      estado: 'Atendida',
      descripcion: 'Solicitud para simplificar funciones y eliminar redundancias.'
    },
    {
      id: 1006,
      usuario: 'Daniel Herrera',
      correo: 'daniel.herrera@email.com',
      tipo: 'Traducción de código',
      fecha: '27/10/2026',
      estado: 'En proceso',
      descripcion: 'Conversión de código JavaScript a Python.'
    }
  ];

  get solicitudesFiltradas(): Solicitud[] {
    const termino = this.busqueda.trim().toLowerCase();

    return this.solicitudes.filter(solicitud => {
      const coincideBusqueda =
        solicitud.usuario.toLowerCase().includes(termino) ||
        solicitud.correo.toLowerCase().includes(termino) ||
        solicitud.tipo.toLowerCase().includes(termino);

      const coincideEstado =
        this.filtroEstado === 'Todas' ||
        solicitud.estado === this.filtroEstado;

      return coincideBusqueda && coincideEstado;
    });
  }

  get totalPendientes(): number {
    return this.solicitudes.filter(s => s.estado === 'Pendiente').length;
  }

  get totalEnProceso(): number {
    return this.solicitudes.filter(s => s.estado === 'En proceso').length;
  }

  get totalAtendidas(): number {
    return this.solicitudes.filter(s => s.estado === 'Atendida').length;
  }

  obtenerClaseEstado(estado: EstadoSolicitud): string {
    switch (estado) {
      case 'Pendiente':
        return 'pending';
      case 'En proceso':
        return 'processing';
      case 'Atendida':
        return 'completed';
    }
  }}
