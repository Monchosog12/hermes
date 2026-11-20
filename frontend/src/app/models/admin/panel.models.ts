export interface Estadistica {
  titulo: string;
  valor: number;
  descripcion: string;
  icono: string;
  variacion?: string;
  color: string;
}

export interface Actividad {
  titulo: string;
  descripcion: string;
  fecha: string;
  icono: string;
  tipo: string;
}

export interface DatoGrafica {
  periodo: string;
  usuarios: number;
  solicitudes: number;
}
