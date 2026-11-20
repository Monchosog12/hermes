/** Modelo de IA disponible para traducir código. */
export interface ModeloIA {
  /** Segmento de la ruta del backend: POST /{endpoint}/traduccion */
  endpoint: string;
  nombre: string;
  descripcion: string;
}
