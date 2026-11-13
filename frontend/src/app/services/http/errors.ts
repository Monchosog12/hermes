import { HttpErrorResponse } from '@angular/common/http';

/** Extrae un mensaje legible de un error HTTP, o devuelve `fallback`. */
export function errorMessage(err: unknown, fallback: string): string {
  if (err instanceof HttpErrorResponse) {
    if (typeof err.error === 'string' && err.error) {
      return err.error;
    }
    if (typeof err.error?.message === 'string' && err.error.message) {
      return err.error.message;
    }
  }
  return fallback;
}
