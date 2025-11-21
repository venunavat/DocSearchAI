package com.ai.agent.service;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class KnowledgeBaseService {

	private final List<String> docs = new ArrayList<>();

	public KnowledgeBaseService() {
		try {
			// Path path = Paths.get("src/main/resources/kb");

			ClassLoader loader = getClass().getClassLoader();
			URL url = loader.getResource("kb");

			Path kbPath = Paths.get(url.toURI());

			Files.walk(kbPath).filter(Files::isRegularFile).forEach(p -> {
				try {
					docs.add(Files.readString(p));
					System.out.println("Loaded docs: " + docs.size());
				} catch (Exception ignored) {
				}
			});
		} catch (Exception ignored) {
		}
	}

	public List<String> getDocs() {
		return docs;
	}
}
