package co.edu.unbosque.hermes.dto;

import java.util.List;

/**
 * Estructura JSON que se le pide a la IA en el prompt:
 * {"codigo": "...", "lenguajeDetectado": "...", "mejoras": ["...", "..."]}
 */
public record RespuestaIA(String codigo, String lenguajeDetectado, List<String> mejoras) {
}
