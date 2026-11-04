import { Component } from '@angular/core';

@Component({
  selector: 'app-headeradmincomponent',
  standalone: false,
  styleUrl: './headeradmincomponent.css',
  templateUrl: './headeradmincomponent.html',
})
export class Headeradmincomponent {
  menuPerfilAbierto = false;

  alternarMenuPerfil(): void {
    this.menuPerfilAbierto = !this.menuPerfilAbierto;
  }
}
