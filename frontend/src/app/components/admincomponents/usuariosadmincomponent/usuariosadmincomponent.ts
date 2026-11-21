import { Component, computed, HostListener, inject, OnInit, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { finalize } from 'rxjs';
import { RolUsuario, User, UsuarioActualizacion } from '../../../models/usuarios.model';
import { UsuarioService } from '../../../services/usuario.service';
import { LoginService } from '../../../services/login.service';

@Component({
  selector: 'app-usuariosadmincomponent',
  standalone: false,
  styleUrl: './usuariosadmincomponent.css',
  templateUrl: './usuariosadmincomponent.html',
})
export class Usuariosadmincomponent implements OnInit {
  private readonly usuarioService = inject(UsuarioService);
  private readonly loginServicio = inject(LoginService);
  private readonly snackBar = inject(MatSnackBar);

  busqueda = '';

  usuarios = signal<User[]>([]);
  totalUsuarios = computed(() => this.usuarios().length);

  /** Nombre del administrador con sesión iniciada (no puede eliminarse a sí mismo). */
  readonly usuarioActual = this.loginServicio.obtenerUsuario();

  readonly roles: { valor: RolUsuario; etiqueta: string }[] = [
    { valor: 'USER', etiqueta: 'Usuario' },
    { valor: 'ADMIN', etiqueta: 'Administrador' },
  ];

  // Ventana de actualización
  usuarioEditando = signal<User | null>(null);
  formulario: UsuarioActualizacion = { username: '', email: '', rol: 'USER', password: '' };
  guardando = false;

  // Confirmación de eliminación
  usuarioAEliminar = signal<User | null>(null);
  eliminando = false;

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.usuarioService.getAll().subscribe({
      next: (respuesta) => {
        this.usuarios.set(Array.isArray(respuesta) ? respuesta : []);
      },
      error: (error) => {
        console.error('Error al consultar los usuarios:', error);
      },
    });
  }

  get usuariosFiltrados(): User[] {
    const termino = this.busqueda.trim().toLowerCase();

    return this.usuarios().filter(
      (user) =>
        user.username?.toLowerCase().includes(termino) ||
        user.email?.toLowerCase().includes(termino)
    );
  }

  esUsuarioActual(user: User): boolean {
    return !!this.usuarioActual && user.username === this.usuarioActual;
  }

  // ---------- Actualizar ----------

  abrirEdicion(user: User): void {
    this.formulario = {
      username: user.username ?? '',
      email: user.email ?? '',
      rol: user.rol ?? 'USER',
      password: '',
    };
    this.usuarioEditando.set(user);
  }

  cerrarEdicion(): void {
    if (this.guardando) return;
    this.usuarioEditando.set(null);
  }

  guardarCambios(form: NgForm): void {
    const usuario = this.usuarioEditando();
    if (!usuario || this.guardando) return;

    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }

    const datos: UsuarioActualizacion = {
      username: this.formulario.username.trim(),
      email: this.formulario.email.trim(),
      rol: this.formulario.rol,
    };
    if (this.formulario.password?.trim()) {
      datos.password = this.formulario.password;
    }

    this.guardando = true;
    this.usuarioService
      .actualizar(usuario.id, datos)
      .pipe(finalize(() => (this.guardando = false)))
      .subscribe({
        next: () => {
          this.usuarios.update((lista) =>
            lista.map((u) =>
              u.id === usuario.id
                ? { ...u, username: datos.username, email: datos.email, rol: datos.rol }
                : u
            )
          );
          this.usuarioEditando.set(null);
          this.mostrarMensaje('Usuario actualizado correctamente.', 'exito');
        },
        error: (error: HttpErrorResponse) => {
          this.mostrarMensaje(this.obtenerMensajeError(error, 'No fue posible actualizar el usuario.'), 'error');
        },
      });
  }

  // ---------- Eliminar ----------

  pedirConfirmacionEliminar(user: User): void {
    this.usuarioAEliminar.set(user);
  }

  cancelarEliminar(): void {
    if (this.eliminando) return;
    this.usuarioAEliminar.set(null);
  }

  confirmarEliminar(): void {
    const usuario = this.usuarioAEliminar();
    if (!usuario || this.eliminando) return;

    this.eliminando = true;
    this.usuarioService
      .eliminar(usuario.id)
      .pipe(finalize(() => (this.eliminando = false)))
      .subscribe({
        next: () => {
          this.usuarios.update((lista) => lista.filter((u) => u.id !== usuario.id));
          this.usuarioAEliminar.set(null);
          this.mostrarMensaje('Usuario eliminado correctamente.', 'exito');
        },
        error: (error: HttpErrorResponse) => {
          this.mostrarMensaje(this.obtenerMensajeError(error, 'No fue posible eliminar el usuario.'), 'error');
        },
      });
  }

  // Cierra la ventana abierta con la tecla Escape.
  @HostListener('document:keydown.escape')
  cerrarVentanas(): void {
    this.cerrarEdicion();
    this.cancelarEliminar();
  }

  // ---------- Utilidades ----------

  private obtenerMensajeError(error: HttpErrorResponse, porDefecto: string): string {
    if (error.status === 0) {
      return 'No hay conexión con el servidor.';
    }
    if (error.status === 401 || error.status === 403) {
      return 'No tienes permisos para esta acción o tu sesión expiró.';
    }
    // El backend responde con texto plano explicando el motivo.
    if (typeof error.error === 'string' && error.error.trim()) {
      return error.error;
    }
    return porDefecto;
  }

  private mostrarMensaje(mensaje: string, tipo: 'exito' | 'error'): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 4000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: tipo === 'exito' ? ['snack-exito'] : ['snack-error'],
    });
  }
}
