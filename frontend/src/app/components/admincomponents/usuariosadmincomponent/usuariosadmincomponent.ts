import { Component, inject, OnInit, signal} from '@angular/core';
import {User} from '../../../models/usuarios.model';
import { UsuarioService } from '../../../services/usuario.service';


@Component({
  selector: 'app-usuariosadmincomponent',
  standalone: false,
  styleUrl: './usuariosadmincomponent.css',
  templateUrl: './usuariosadmincomponent.html',
})
export class Usuariosadmincomponent implements OnInit {
  private readonly usuarioService = inject(UsuarioService);

  busqueda = '';
  filtroEstado: 'Todos' | 'Activo' | 'Inactivo' = 'Todos';

  // 2. Convierte usuarios en un signal
  usuarios = signal<User[]>([]);

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.usuarioService.getAll().subscribe({
      next: (respuesta) => {
        console.log('Respuesta recibida:', respuesta);
        // 3. Usa .set() para actualizar el signal y notificar a la vista
        this.usuarios.set(Array.isArray(respuesta) ? respuesta : []);
      },
      error: (error) => {
        console.error('Error al consultar los usuarios:', error);
      },
    });
  }

  // 4. Lee el signal usando this.usuarios() en tus getters
  get usuariosFiltrados(): User[] {
    const termino = this.busqueda.trim().toLowerCase();

    return this.usuarios().filter((user) => {
      const coincideBusqueda =
        user.username?.toLowerCase().includes(termino) ||
        user.email?.toLowerCase().includes(termino);

      const estado = user.cuentaNoExpirada ? 'Activo' : 'Inactivo';
      const coincideEstado = this.filtroEstado === 'Todos' || estado === this.filtroEstado;

      return coincideBusqueda && coincideEstado;
    });
  }

  get totalActivos(): number {
    return this.usuarios().filter((user) => user.cuentaNoExpirada).length;
  }

  get totalInactivos(): number {
    return this.usuarios().filter((user) => !user.cuentaNoExpirada).length;
  }
}
