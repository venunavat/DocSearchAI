package com.ai.agent.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LLMService {

	private static final String GEMINI_API_KEY = "AIzaSyDovXFrsF3JBxdtZDxxvoImJc_KwZVWpec";
	// private static final String GEMINI_LLM_URL =
	// "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
	// + GEMINI_API_KEY;

	private static final String GEMINI_LLM_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
			+ GEMINI_API_KEY;

	// https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=YOUR_API_KEY

	private final HttpClient client = HttpClient.newHttpClient();
	private final ObjectMapper mapper = new ObjectMapper();

	public String askGemini(String prompt) {
		try {
			String json = """
					{
					  "contents": [{
					    "parts": [{"text": "%s"}]
					  }]
					}
					""".formatted(prompt.replace("\"", "'"));

			HttpRequest req = HttpRequest.newBuilder().uri(URI.create(GEMINI_LLM_URL))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();

			HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
			System.out.println("Gemini raw response: " + resp.body());
			JsonNode root = mapper.readTree(resp.body());
			return root.at("/candidates/0/content/parts/0/text").asText("no response");

		} catch (Exception e) {
			return "Gemini Error: " + e.getMessage();
		}
	}
}
