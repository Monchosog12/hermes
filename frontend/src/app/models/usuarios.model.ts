export type RolUsuario = 'USER' | 'ADMIN';

export interface User {
  id: number;
  email: string;
  username: string;
  rol?: RolUsuario;
}

/** Datos que se envían al actualizar un usuario desde el panel de administración. */
export interface UsuarioActualizacion {
  username: string;
  email: string;
  rol: RolUsuario;
  /** Opcional: si va vacío se conserva la contraseña actual. */
  password?: string;
}
