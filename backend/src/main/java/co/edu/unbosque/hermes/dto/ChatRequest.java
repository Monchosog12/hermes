package co.edu.unbosque.hermes.dto;

import java.util.List;

public record ChatRequest(String model, List<ChatMessage> messages) {
}
