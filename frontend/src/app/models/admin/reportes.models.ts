export interface ReporteUsuario {
  id: number;
  nombre: string;
  correo: string;
  fechaRegistro: string;
  estado: 'Activo' | 'Inactivo';
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
