import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-headercomponent',
  standalone: false,
  templateUrl: './headercomponent.html',
  styleUrl: './headercomponent.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Headercomponent {
  protected readonly auth = inject(AuthService);
}
