export interface Usuario {
  id: number;
  nombre: string;
  correo: string;
  fechaRegistro: string;
  estado: 'Activo' | 'Inactivo';
}
