/** Traducción guardada en el historial del usuario (respuesta de GET /historial). */
export interface TraduccionHistorial {
  id: number;
  codigoOriginal: string;
  codigoTraducido: string;
  lenguajeOrigen: string;
  lenguajeDestino: string;
  modelo: string | null;
  /** Fecha ISO enviada por el backend, ej. "2026-11-20T15:41:03". */
  fecha: string | null;
}

/** Campos que se pueden modificar de una traducción del historial. */
export interface TraduccionHistorialActualizacion {
  codigoOriginal: string;
  codigoTraducido: string;
  lenguajeOrigen: string;
  lenguajeDestino: string;
}

/** Lenguajes disponibles en el traductor (mismos códigos que guarda el backend). */
export const LENGUAJES_HISTORIAL: { valor: string; etiqueta: string }[] = [
  { valor: 'JAVASCRIPT', etiqueta: 'JavaScript' },
  { valor: 'TYPESCRIPT', etiqueta: 'TypeScript' },
  { valor: 'JAVA', etiqueta: 'Java' },
  { valor: 'PYTHON', etiqueta: 'Python' },
  { valor: 'CPP', etiqueta: 'C++' },
  { valor: 'CSHARP', etiqueta: 'C#' },
];
