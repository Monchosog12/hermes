export type TipoOperacion = 'Traducción' | 'Mejora';
export type Lenguaje = 'Java' | 'JavaScript' | 'Python';

export interface ArchivoHistorial {
  id: number;
  nombre: string;
  lenguaje: Lenguaje;
  operacion: TipoOperacion;
  fecha: string;
  tamano: string;
  extension: string;
}
