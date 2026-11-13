import { ChangeDetectionStrategy, Component } from '@angular/core';
import { Operation } from '../ai/ai.models';
import { Role, UserStatus, UserSummary } from '../auth/auth.models';

export interface AdminUser extends UserSummary {
  createdAt: string;
}

export interface UpdateUserRequest {
  role?: Role;
  status?: UserStatus;
}

export interface ModelUsage {
  modelId: string;
  modelLabel: string;
  requests: number;
  avgLatencyMs: number;
}

export interface OperationUsage {
  operation: Operation;
  requests: number;
}

export interface UsageStats {
  totalUsers: number;
  totalRequests: number;
  byModel: ModelUsage[];
  byOperation: OperationUsage[];
}

export interface AuditLog {
  id: number;
  userId: number | null;
  action: string;
  statusCode: number;
  ipAddress: string;
  country: string | null;
  city: string | null;
  latitude: number | null;
  longitude: number | null;
  mapsUrl: string | null;
  createdAt: string;
}

export interface DateRange {
  from: string; // yyyy-MM-dd
  to: string;
}

export function lastDays(days: number, today: Date = new Date()): DateRange {
  const toIso = (d: Date): string => d.toISOString().slice(0, 10);
  const from = new Date(today);
  from.setDate(from.getDate() - days);
  return { from: toIso(from), to: toIso(today) };
}

export const USER_STATUS_LABELS: Readonly<Record<UserStatus, string>> = {
  PENDING_VERIFICATION: 'Pendiente de confirmar el correo',
  ACTIVE: 'Activo',
  BLOCKED: 'Bloqueado (no puede iniciar sesión)',
};

@Component({
  selector: 'app-adminmodels',
  standalone: false,
  templateUrl: './admin.models.html',
  styleUrl: './admin.models.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Adminmodels {
  protected readonly statuses = (Object.keys(USER_STATUS_LABELS) as UserStatus[]).map((code) => ({
    code,
    label: USER_STATUS_LABELS[code],
  }));
}
