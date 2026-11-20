import { UserSummary } from './auth.models';

export interface Session {
  accessToken: string;
  expiresAt: number; // epoch ms
  user: UserSummary;
}
