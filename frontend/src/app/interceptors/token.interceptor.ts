import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { LoginService } from '../services/login.service';

/** Agrega "Authorization: Bearer <token>" a las peticiones cuando hay sesión iniciada. */
export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(LoginService).obtenerToken();

  // Login y registro son públicos: no necesitan el token.
  if (!token || req.url.includes('/auth/')) {
    return next(req);
  }

  return next(req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));
};
