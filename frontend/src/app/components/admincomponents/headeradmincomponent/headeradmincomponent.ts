import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from '../../../services/login.service';

@Component({
  selector: 'app-headeradmincomponent',
  standalone: false,
  styleUrl: './headeradmincomponent.css',
  templateUrl: './headeradmincomponent.html',
})
export class Headeradmincomponent {
  private readonly loginServicio = inject(LoginService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  menuPerfilAbierto = false;

  get usuarioActual(): string {
    return this.loginServicio.obtenerUsuario() ?? 'Administrador';
  }

  alternarMenuPerfil(): void {
    this.menuPerfilAbierto = !this.menuPerfilAbierto;
  }

  cerrarSesion(): void {
    this.menuPerfilAbierto = false;
    this.loginServicio.cerrarSesion();
    this.snackBar.open('Sesión cerrada.', 'Cerrar', {
      duration: 4000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['snack-exito'],
    });
    this.router.navigate(['/usuario/traductor']);
  }
}
