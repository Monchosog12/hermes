export type EstadoSolicitud = 'Pendiente' | 'En proceso' | 'Atendida';

export interface Solicitud {
  id: number;
  usuario: string;
  correo: string;
  tipo: string;
  fecha: string;
  estado: EstadoSolicitud;
  descripcion: string;
}
