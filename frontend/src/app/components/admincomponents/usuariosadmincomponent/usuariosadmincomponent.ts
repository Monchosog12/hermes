import { Component } from '@angular/core';
interface Usuario {
  id: number;
  nombre: string;
  correo: string;
  fechaRegistro: string;
  estado: 'Activo' | 'Inactivo';
}
@Component({
  selector: 'app-usuariosadmincomponent',
  standalone: false,
  styleUrl: './usuariosadmincomponent.css',
  templateUrl: './usuariosadmincomponent.html',
})
export class Usuariosadmincomponent {
  busqueda = '';
  filtroEstado: 'Todos' | 'Activo' | 'Inactivo' = 'Todos';

  usuarios: Usuario[] = [
    {
      id: 1,
      nombre: 'Laura Martínez',
      correo: 'laura.martinez@email.com',
      fechaRegistro: '12/09/2026',
      estado: 'Activo'
    },
    {
      id: 2,
      nombre: 'Carlos Rodríguez',
      correo: 'carlos.rodriguez@email.com',
      fechaRegistro: '18/09/2026',
      estado: 'Activo'
    },
    {
      id: 3,
      nombre: 'Andrés Gómez',
      correo: 'andres.gomez@email.com',
      fechaRegistro: '25/09/2026',
      estado: 'Inactivo'
    },
    {
      id: 4,
      nombre: 'Mariana López',
      correo: 'mariana.lopez@email.com',
      fechaRegistro: '02/10/2026',
      estado: 'Activo'
    },
    {
      id: 5,
      nombre: 'Daniel Herrera',
      correo: 'daniel.herrera@email.com',
      fechaRegistro: '08/10/2026',
      estado: 'Inactivo'
    },
    {
      id: 6,
      nombre: 'Sofía Ramírez',
      correo: 'sofia.ramirez@email.com',
      fechaRegistro: '15/10/2026',
      estado: 'Activo'
    }
  ];

  get usuariosFiltrados(): Usuario[] {
    const termino = this.busqueda.trim().toLowerCase();

    return this.usuarios.filter(usuario => {
      const coincideBusqueda =
        usuario.nombre.toLowerCase().includes(termino) ||
        usuario.correo.toLowerCase().includes(termino);

      const coincideEstado =
        this.filtroEstado === 'Todos' ||
        usuario.estado === this.filtroEstado;

      return coincideBusqueda && coincideEstado;
    });
  }

  get totalActivos(): number {
    return this.usuarios.filter(usuario =>
      usuario.estado === 'Activo'
    ).length;
  }

  get totalInactivos(): number {
    return this.usuarios.filter(usuario =>
      usuario.estado === 'Inactivo'
    ).length;
  }
}
