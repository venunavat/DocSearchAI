package com.ai.agent.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UIController {

	private final RagController ragController;

	public UIController(RagController ragController) {
		this.ragController = ragController;
	}

	@GetMapping("/")
	public String home() {
		return "index";
	}

	@PostMapping("/ask-ui")
	public String askUI(@RequestParam("question") String question, Model model) {

		Map<String, Object> response = ragController.ask(Map.of("question", question));

		model.addAttribute("question", question);
		model.addAttribute("answer", response.get("answer"));

		model.addAttribute("context_similarity", response.get("context_similarity"));
		model.addAttribute("context_used", response.get("context_used"));

		return "index";
	}
}
