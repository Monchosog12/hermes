/** Página de resultados tal como la serializa Spring Data (org.springframework.data.domain.Page). */
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number; // índice de página, base 0
  size: number;
}
