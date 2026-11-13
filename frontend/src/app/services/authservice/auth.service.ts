import { HttpClient } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
  RegisterResponse,
  UserSummary,
  ROLE_LABELS,
} from '../../models/auth/auth.models';

interface Session {
  accessToken: string;
  expiresAt: number; // epoch ms
  user: UserSummary;
}

const STORAGE_KEY = 'codeforge.session';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly api = `${environment.apiUrl}/auth`;

  private readonly session = signal<Session | null>(this.restore());

  readonly user = computed(() => this.session()?.user ?? null);
  readonly token = computed(() => this.session()?.accessToken ?? null);
  readonly isAuthenticated = computed(() => this.session() !== null);
  readonly isAdmin = computed(() => this.user()?.role === 'ADMIN');

  register(body: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.api}/register`, body);
  }

  confirmEmail(token: string): Observable<void> {
    return this.http.post<void>(`${this.api}/confirm`, { token });
  }

  login(body: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.api}/login`, body)
      .pipe(tap((res) => this.setSession(res)));
  }

  forgotPassword(email: string): Observable<void> {
    return this.http.post<void>(`${this.api}/forgot-password`, { email });
  }

  resetPassword(token: string, newPassword: string): Observable<void> {
    return this.http.post<void>(`${this.api}/reset-password`, { token, newPassword });
  }

  logout(): void {
    this.session.set(null);
    this.write(null);
    void this.router.navigate(['/auth/login']);
  }

  private setSession(res: AuthResponse): void {
    const session: Session = {
      accessToken: res.accessToken,
      expiresAt: Date.now() + res.expiresIn * 1000,
      user: res.user,
    };
    this.session.set(session);
    this.write(session);
  }

  private restore(): Session | null {
    try {
      const raw = sessionStorage.getItem(STORAGE_KEY);
      if (!raw) {
        return null;
      }
      const session = JSON.parse(raw) as Session;
      return session.expiresAt > Date.now() ? session : null;
    } catch {
      return null;
    }
  }

  private write(session: Session | null): void {
    try {
      if (session) {
        sessionStorage.setItem(STORAGE_KEY, JSON.stringify(session));
      } else {
        sessionStorage.removeItem(STORAGE_KEY);
      }
    } catch {
    }
  }
}

@Component({
  selector: 'app-authservice',
  standalone: false,
  templateUrl: './auth.service.html',
  styleUrl: './auth.service.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Authservice {
  protected readonly auth = inject(AuthService);
  protected readonly roleLabels = ROLE_LABELS;
}
