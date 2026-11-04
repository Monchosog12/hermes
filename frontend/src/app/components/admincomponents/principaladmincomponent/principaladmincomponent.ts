import {Component} from '@angular/core';

interface Estadistica {
  titulo: string;
  valor: number;
  descripcion: string;
  icono: string;
  variacion?: string;
  color: string;
}

interface Actividad {
  titulo: string;
  descripcion: string;
  fecha: string;
  icono: string;
  tipo: string;
}
interface DatoGrafica {
  periodo: string;
  usuarios: number;
  solicitudes: number;
}

@Component({
  selector: 'app-principaladmincomponent',
  standalone: false,
  styleUrl: './principaladmincomponent.css',
  templateUrl: './principaladmincomponent.html',
})
export class Principaladmincomponent {
  estadisticas: Estadistica[] = [
    {
      titulo: 'Usuarios registrados',
      valor: 128,
      descripcion: 'Usuarios en la plataforma',
      icono: 'fa-solid fa-users',
      variacion: '+12% este mes',
      color: 'purple'
    },
    {
      titulo: 'Solicitudes recibidas',
      valor: 346,
      descripcion: 'Total de solicitudes',
      icono: 'fa-solid fa-file-lines',
      variacion: '+8% este mes',
      color: 'blue'
    },
    {
      titulo: 'Solicitudes pendientes',
      valor: 12,
      descripcion: 'Requieren atención',
      icono: 'fa-solid fa-clock',
      color: 'orange'
    },
    {
      titulo: 'Solicitudes atendidas',
      valor: 334,
      descripcion: 'Solicitudes completadas',
      icono: 'fa-solid fa-circle-check',
      variacion: '96,5% del total',
      color: 'green'
    }
  ];

  totalSolicitudes = 346;
  solicitudesAtendidas = 334;
  solicitudesPendientes = 12;

  // Datos ficticios para la gráfica de actividad mensual.
  datosGrafica: DatoGrafica[] = [
    {periodo: 'May', usuarios: 35, solicitudes: 45},
    {periodo: 'Jun', usuarios: 48, solicitudes: 58},
    {periodo: 'Jul', usuarios: 55, solicitudes: 65},
    {periodo: 'Ago', usuarios: 62, solicitudes: 72},
    {periodo: 'Sep', usuarios: 78, solicitudes: 85},
    {periodo: 'Oct', usuarios: 92, solicitudes: 100}
  ];

  actividadesRecientes: Actividad[] = [
    {
      titulo: 'Nuevo usuario registrado',
      descripcion: 'Se ha registrado un nuevo usuario en la plataforma.',
      fecha: 'Hace 10 minutos',
      icono: 'fa-solid fa-user-plus',
      tipo: 'purple'
    },
    {
      titulo: 'Nueva solicitud recibida',
      descripcion: 'Un usuario ha enviado una nueva solicitud.',
      fecha: 'Hace 35 minutos',
      icono: 'fa-solid fa-inbox',
      tipo: 'blue'
    },
    {
      titulo: 'Solicitud atendida',
      descripcion: 'Una solicitud ha sido marcada como completada.',
      fecha: 'Hace 1 hora',
      icono: 'fa-solid fa-check',
      tipo: 'green'
    },
    {
      titulo: 'Nuevo usuario registrado',
      descripcion: 'Se ha creado una nueva cuenta.',
      fecha: 'Hace 3 horas',
      icono: 'fa-solid fa-user-plus',
      tipo: 'purple'
    }
  ];

  // Calcula la altura de las barras tomando como referencia el valor máximo.
  obtenerAltura(valor: number): string {
    const maximo = 100;
    return `${(valor / maximo) * 100}%`;
  }

  obtenerPorcentaje(valor: number): number {
    return (valor / this.totalSolicitudes) * 100;
  }
}
