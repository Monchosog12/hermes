import { Component, signal } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';


@Component({
  selector: 'app-root',
  standalone: false,
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('hermes');
  mostrarHeaderPublico = true;

  constructor(private router: Router) {

    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd)
      )
      .subscribe((event) => {

        const rutaActual = (event as NavigationEnd).urlAfterRedirects;

        this.mostrarHeaderPublico = !rutaActual.startsWith('/admin');

      });

  }
}
