export interface AnswertraduccionModel {
  id: number;
  codigoOriginal: string;
  codigoTraducido: string;
  lenguajeOrigen: string;
  lenguajeDestino: string;
  mejoras: string[];
  modelo: string;
  fecha: string;
}
