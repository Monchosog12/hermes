import { ValidatorFn, Validators } from '@angular/forms';

export const PASSWORD_MIN_LENGTH = 8;

export const PASSWORD_HINT = `mínimo ${PASSWORD_MIN_LENGTH} caracteres.`;

export const PASSWORD_VALIDATORS: ValidatorFn[] = [
  Validators.required,
  Validators.minLength(PASSWORD_MIN_LENGTH),
];
