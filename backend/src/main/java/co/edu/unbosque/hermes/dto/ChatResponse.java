package co.edu.unbosque.hermes.dto;

import java.util.List;

public record ChatResponse(List<Choice> choices) {

	public record Choice(ChatMessage message) {
	}
}
