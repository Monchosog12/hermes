import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RegisterService } from '../../../services/register.service';
import { RegisterModel } from '../../../models/register.model';
import { finalize } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-headercomponent',
  standalone: false,
  styleUrl: './headercomponent.css',
  templateUrl: './headercomponent.html',
})
export class Headercomponent {
  private readonly fb = inject(FormBuilder);
  private readonly servicio = inject(RegisterService);
  private readonly snackBar = inject(MatSnackBar);

  cargando = false;

  formulario = this.fb.nonNullable.group({
    username: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
  });
  registrar(): void {
    if (this.cargando) return;

    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const datos: RegisterModel = this.formulario.getRawValue();
    this.cargando = true;

    this.servicio
      .register(datos)
      .pipe(finalize(() => (this.cargando = false)))
      .subscribe({
        next: () => {
          this.mostrarMensaje('Usuario registrado correctamente.', 'exito');
          this.formulario.reset();
        },
        error: (error: HttpErrorResponse) => {
          this.mostrarMensaje(this.obtenerMensajeError(error), 'error');
        },
      });
  }

  get username() {
    return this.formulario.controls.username;
  }
  get email() {
    return this.formulario.controls.email;
  }
  get password() {
    return this.formulario.controls.password;
  }
  private mostrarMensaje(mensaje: string, tipo: 'exito' | 'error'): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 4000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: tipo === 'exito' ? ['snack-exito'] : ['snack-error'],
    });
  }
  private obtenerMensajeError(error: HttpErrorResponse): string {
    switch (error.status) {
      case 0:
        return 'No hay conexión con el servidor.';
      case 400:
        return 'Los datos enviados no son válidos.';
      case 403:
        return 'No tienes permiso para realizar esta acción.';
      case 409:
        return 'El usuario o el correo ya están registrados.';
      default:
        return 'No fue posible registrar el usuario. Intenta de nuevo.';
    }
  }
}

