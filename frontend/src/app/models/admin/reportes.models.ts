export interface ReporteUsuario {
  id: number;
  nombre: string;
  correo: string;
  fechaRegistro: string;
}

export interface ReporteSolicitud {
  id: number;
  usuario: string;
  tipo: string;
  fecha: string;
  estado: 'Pendiente' | 'En proceso' | 'Atendida';
}

export interface EstadisticaMensual {
  mes: string;
  cantidad: number;
}
