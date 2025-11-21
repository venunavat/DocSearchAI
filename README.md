Step-by-Step Instructions
1. Set up the Spring Boot project
Create a new Spring Boot project with Web starter.
Add dependencies for HTTP client, JSON processing, Spring Web.
2. Build the KnowledgeBaseService
Read documents from resources/kb/ folder at startup.
Store each document’s content and metadata (filename, path).
Example snippet:
Path kbDir = Paths.get("src/main/resources/kb/"); Files.walk(kbDir).forEach(path -> { if (Files.isRegularFile(path)) { String content = Files.readString(path); // store in memory list } });
3. Build EmbeddingService
For each document and for each user query, call the embeddings API.
Compute cosine similarity between query embedding and each document embedding.
Select top-1 (or top-k) document(s).
double score = cosineSimilarity(queryEmbedding, docEmbedding);
4. Build LLMService
Take the retrieved document text + user question.
Send prompt to the LLM:
“Context: <document content>\n\nQuestion: <user question>”
Receive answer and return to the client.
5. Build the API Controller
Expose endpoint: POST /api/ask with JSON body:{ "question": "..."}.
Controller steps:
Retrieve docs → embeddings
Select best doc
Call LLM
Return JSON: { "answer": "...", "source": "filename.txt", "score": 0.87 }
6. Build UI Controller (optional)
A simple web page (/index.html): input box + “Ask” button + answer display.
On form submit, call the API and show results.
7. Containerize and Deploy to Cloud Run
Create Dockerfile:
FROM eclipse-temurin:17-jdk COPY target/ai-knowledge-agent.jar /app/app.jar ENTRYPOINT ["java","-jar","/app/app.jar"]
Build image, push to Container Registry, deploy to Cloud Run.
Set environment variables (e.g., API keys).
Ensure --allow-unauthenticated (or secure accordingly).
8. Demo test
Upload a few .txt knowledge files (e.g., “cloud_run.txt”, “devops_faq.txt”).
Ask a relevant question: “What is Cloud Run?”
Observe the system retrieving the correct document, generating an answer, and showing the source + score.
Result / Demo
Once your service is live you’ll see a neat web UI like this:

Input box: “What is Cloud Run?”
Answer appears: “Cloud run is …”
Metadata: Source file “devops_runbook.txt”, Similarity score: 0.91
This demonstrates how DocSearchAI delivers enterprise-ready answers from internal docs — fast, accurate, and grounded in your knowledge base.
