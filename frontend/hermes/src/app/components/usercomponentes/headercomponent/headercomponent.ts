import { Component, inject } from '@angular/core';
import { AuthLoginService } from '../../../service/authlogin.service';
import { Router } from '@angular/router';
import { Authlogin } from '../../../models/authlogin.model';
import { Register } from '../../../models/register.model';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { RegisterService } from '../../../service/register.service';
import { AnswerregisterModel } from '../../../models/answerregister.model';

@Component({
  selector: 'app-headercomponent',
  standalone: false,
  styleUrl: './headercomponent.css',
  templateUrl: './headercomponent.html',
})
export class Headercomponent {
  registrar: FormGroup;
  inicio: FormGroup;
  mensajeServidor: string = '';
  tipoMensaje: 'success' | 'error' | '' = '';

  constructor(
    private fb: FormBuilder,
    private authLoginService: AuthLoginService,
    private registerService: RegisterService,
  ) {
    this.registrar = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });

    this.inicio = this.fb.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }
  onSubmit() {
    if (this.registrar.valid) {
      this.mensajeServidor = '';
      this.tipoMensaje = '';
      const datos: Register = this.registrar.value as Register;

      this.registerService.registro(datos).subscribe({
        next: (respuesta) => {
          this.mensajeServidor = respuesta.mensaje;
          this.tipoMensaje = 'success';
          this.registrar.reset();
        },
        error: (error) => {
          console.error('Respuesta de error:', error);
          console.error('Cuerpo del error:', error.error);

          this.mensajeServidor = error.error?.mensaje ?? 'No se pudo crear el usuario';

          this.tipoMensaje = 'error';
        },
      });
    }
  }
  login() {
    if (this.inicio.valid) {
      this.mensajeServidor = '';
      this.tipoMensaje = '';

      const datos: Authlogin = this.inicio.value as Authlogin;

      this.authLoginService.iniciarSesion(datos).subscribe({
        next: (respuesta) => {
          console.log('Respuesta del servidor:', respuesta);

          this.mensajeServidor = 'Inicio de sesión exitoso';
          this.tipoMensaje = 'success';

          this.inicio.reset();
        },

        error: (error) => {
          console.error('Error al iniciar sesión:', error);
          console.error('Cuerpo del error:', error.error);

          this.mensajeServidor = error.error?.mensaje ?? 'No se pudo iniciar sesión';

          this.tipoMensaje = 'error';
        },
      });
    }
  }
}
