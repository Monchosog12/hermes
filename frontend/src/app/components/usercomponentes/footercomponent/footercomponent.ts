import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-footercomponent',
  standalone: false,
  templateUrl: './footercomponent.html',
  styleUrl: './footercomponent.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Footercomponent {
  protected readonly year = new Date().getFullYear();
}
