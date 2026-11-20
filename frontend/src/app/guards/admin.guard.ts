import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from '../services/login.service';

/** Solo deja entrar a las rutas /admin si hay sesión iniciada con rol ADMIN. */
export const adminGuard: CanActivateFn = () => {
  const loginServicio = inject(LoginService);

  if (loginServicio.estaLogueado() && loginServicio.obtenerRol() === 'ADMIN') {
    return true;
  }

  inject(MatSnackBar).open('No tienes permiso para acceder al panel de administración.', 'Cerrar', {
    duration: 4000,
    horizontalPosition: 'right',
    verticalPosition: 'top',
    panelClass: ['snack-error'],
  });
  return inject(Router).parseUrl('/usuario/traductor');
};
