package com.ai.agent.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmbeddingService {

	private static final String GEMINI_API_KEY = "AIzaSyDovXFrsF3JBxdtZDxxvoImJc_KwZVWpec";
	private static final String GEMINI_EMBED_URL = "https://generativelanguage.googleapis.com/v1beta/models/text-embedding-004:embedContent?key="
			+ GEMINI_API_KEY;

	// private static final String GEMINI_EMBED_URL =
	// "https://generativelanguage.googleapis.com/v1beta/models/text-embedding-004:embedContent?key="
	// + GEMINI_API_KEY;

	// https://generativelanguage.googleapis.com/v1beta/models/text-embedding-004:embedContent?key=YOUR_API_KEY

	private final ObjectMapper mapper = new ObjectMapper();
	private final HttpClient client = HttpClient.newHttpClient();

	public double[] embed(String text) {
		try {
			String body = """
					{
					  "content": { "parts": [{"text": "%s"}] }
					}
					""".formatted(text.replace("\"", "'"));

			HttpRequest req = HttpRequest.newBuilder().uri(URI.create(GEMINI_EMBED_URL))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(body)).build();

			HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
			JsonNode vec = mapper.readTree(resp.body()).at("/embedding/values");

			double[] arr = new double[vec.size()];
			for (int i = 0; i < vec.size(); i++)
				arr[i] = vec.get(i).asDouble();
			return arr;

		} catch (Exception e) {
			throw new RuntimeException("Embedding failed: " + e.getMessage());
		}
	}

//	public double cosine(double[] a, double[] b) {
//		double dot = 0, na = 0, nb = 0;
//		for (int i = 0; i < a.length; i++) {
//			dot += a[i] * b[i];
//			na += a[i] * a[i];
//			nb += b[i] * b[i];
//		}
//		return dot / (Math.sqrt(na) * Math.sqrt(nb));
//	}

	public double cosine(double[] a, double[] b) {

		if (a == null || b == null || a.length == 0 || b.length == 0) {
			return -1.0; // invalid similarity
		}

		double dot = 0, na = 0, nb = 0;

		for (int i = 0; i < a.length; i++) {
			dot += a[i] * b[i];
			na += a[i] * a[i];
			nb += b[i] * b[i];
		}

		return dot / (Math.sqrt(na) * Math.sqrt(nb));
	}

}
