import {Component} from '@angular/core';

type TipoOperacion = 'Traducción' | 'Mejora';
type Lenguaje = 'Java' | 'JavaScript' | 'Python';

interface ArchivoHistorial {
  id: number;
  nombre: string;
  lenguaje: Lenguaje;
  operacion: TipoOperacion;
  fecha: string;
  tamano: string;
  extension: string;
}

@Component({
  selector: 'app-historialcomponent',
  standalone: false,
  styleUrl: './historialcomponent.css',
  templateUrl: './historialcomponent.html',
})
export class Historialcomponent {
  busqueda = '';
  filtroLenguaje = 'todos';
  filtroOperacion = 'todas';

  archivos: ArchivoHistorial[] = [
    {
      id: 1,
      nombre: 'codigo_optimizado',
      lenguaje: 'Java',
      operacion: 'Mejora',
      fecha: '02 nov. 2026',
      tamano: '12 KB',
      extension: '.java'
    },
    {
      id: 2,
      nombre: 'traductor_script',
      lenguaje: 'Python',
      operacion: 'Traducción',
      fecha: '01 nov. 2026',
      tamano: '8 KB',
      extension: '.py'
    },
    {
      id: 3,
      nombre: 'app_refactorizado',
      lenguaje: 'JavaScript',
      operacion: 'Mejora',
      fecha: '30 oct. 2026',
      tamano: '15 KB',
      extension: '.js'
    },
    {
      id: 4,
      nombre: 'algoritmo_conversion',
      lenguaje: 'Java',
      operacion: 'Traducción',
      fecha: '28 oct. 2026',
      tamano: '10 KB',
      extension: '.java'
    },
    {
      id: 5,
      nombre: 'funciones_limpias',
      lenguaje: 'Python',
      operacion: 'Mejora',
      fecha: '26 oct. 2026',
      tamano: '6 KB',
      extension: '.py'
    },
    {
      id: 6,
      nombre: 'calculadora_web',
      lenguaje: 'JavaScript',
      operacion: 'Traducción',
      fecha: '24 oct. 2026',
      tamano: '18 KB',
      extension: '.js'
    }
  ];

  get archivosFiltrados(): ArchivoHistorial[] {
    return this.archivos.filter(archivo => {
      const coincideNombre = archivo.nombre
        .toLowerCase()
        .includes(this.busqueda.toLowerCase());

      const coincideLenguaje =
        this.filtroLenguaje === 'todos' ||
        archivo.lenguaje === this.filtroLenguaje;

      const coincideOperacion =
        this.filtroOperacion === 'todas' ||
        archivo.operacion === this.filtroOperacion;

      return coincideNombre && coincideLenguaje && coincideOperacion;
    });
  }

  get totalTraducciones(): number {
    return this.archivos.filter(
      archivo => archivo.operacion === 'Traducción'
    ).length;
  }

  get totalMejoras(): number {
    return this.archivos.filter(
      archivo => archivo.operacion === 'Mejora'
    ).length;
  }

  get totalLenguajes(): number {
    return new Set(this.archivos.map(archivo => archivo.lenguaje)).size;
  }

  obtenerIcono(lenguaje: Lenguaje): string {
    const iconos: Record<Lenguaje, string> = {
      Java: 'fa-brands fa-java',
      JavaScript: 'fa-brands fa-js',
      Python: 'fa-brands fa-python'
    };

    return iconos[lenguaje];
  }

  limpiarFiltros(): void {
    this.busqueda = '';
    this.filtroLenguaje = 'todos';
    this.filtroOperacion = 'todas';
  }

}
