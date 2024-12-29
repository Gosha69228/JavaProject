package org.example.service;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class LLMClient {

    private static final String API_URL = "https://api-inference.huggingface.co/models/mistralai/Mistral-Nemo-Instruct-2407";
    private static final String API_KEY = "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"; // Ваш ключ API

    public String getAdviceFromModel(String prompt) throws IOException, InterruptedException {
        String jsonRequestBody = "{\"inputs\": \"" + prompt + "\"}";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                .build();

        // Отправляем запрос и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Парсим JSON-ответ
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, String>> responseList = objectMapper.readValue(response.body(), new TypeReference<List<Map<String, String>>>() {});

        // Извлекаем текст ответа модели
        if (!responseList.isEmpty()) {
            return responseList.get(0).get("generated_text").substring(prompt.length());
        } else {
            throw new RuntimeException("Ответ модели не содержит данных.");
        }
    }
}