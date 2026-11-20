import { ChangeDetectionStrategy, Component } from '@angular/core';
import { PASSWORD_HINT } from './password.validators';
// Interfaces que reflejan 1:1 los DTO del backend (com.codeforge.auth.dto / user).
export type Role = 'CLIENT' | 'ADMIN';
export type UserStatus = 'PENDING_VERIFICATION' | 'ACTIVE' | 'BLOCKED';

export interface UserSummary {
  id: number;
  fullName: string;
  email: string;
  role: Role;
  status: UserStatus;
}

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
}

export interface RegisterResponse {
  id: number;
  email: string;
  status: UserStatus;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number; // segundos
  user: UserSummary;
}

export const ROLE_LABELS: Readonly<Record<Role, string>> = {
  CLIENT: 'Cliente',
  ADMIN: 'Administrador',
};

const ROLE_PERMISSIONS: Readonly<Record<Role, string>> = {
  CLIENT: 'Traduce, analiza y formatea código; consulta su historial; descarga o envía los resultados por correo.',
  ADMIN: 'Todo lo anterior, y además gestiona usuarios, consulta estadísticas, auditoría y reportes en PDF.',
};

/** Explica los roles de la plataforma y la regla de contraseñas (pantalla de ayuda). */
@Component({
  selector: 'app-authmodels',
  standalone: false,
  templateUrl: './auth.models.html',
  styleUrl: './auth.models.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Authmodels {
  protected readonly roles = (Object.keys(ROLE_LABELS) as Role[]).map((role) => ({
    role,
    label: ROLE_LABELS[role],
    permissions: ROLE_PERMISSIONS[role],
  }));
  protected readonly passwordHint = PASSWORD_HINT;
}
