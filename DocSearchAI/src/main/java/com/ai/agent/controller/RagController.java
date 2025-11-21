package com.ai.agent.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.agent.service.EmbeddingService;
import com.ai.agent.service.KnowledgeBaseService;
import com.ai.agent.service.LLMService;

@RestController
@RequestMapping("/api")
public class RagController {

	private final EmbeddingService embed;
	private final KnowledgeBaseService kb;
	private final LLMService llm;

	private final List<String> docs;
	private final List<double[]> vectors;

	public RagController(EmbeddingService embed, KnowledgeBaseService kb, LLMService llm) {
		this.embed = embed;
		this.kb = kb;
		this.llm = llm;

		docs = kb.getDocs();
		vectors = docs.stream().map(embed::embed).toList();
	}

	@PostMapping("/ask")
	public Map<String, Object> ask(@RequestBody Map<String, String> body) {

		String question = body.get("question");
		double[] qVec = embed.embed(question);

		double best = -1;
		String bestDoc = "";

		for (int i = 0; i < docs.size(); i++) {
			double sim = embed.cosine(qVec, vectors.get(i));
			if (sim > best) {
				best = sim;
				bestDoc = docs.get(i);
			}
		}

		String prompt = """
				Use the following knowledge base context:

				%s

				Question: %s
				""".formatted(bestDoc, question);

		String answer = llm.askGemini(prompt);

		return Map.of("question", question, "context_similarity", best, "context_used", bestDoc, "answer", answer);
	}
}
