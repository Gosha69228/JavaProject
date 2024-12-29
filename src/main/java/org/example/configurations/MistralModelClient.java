package org.example.configurations;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.IOException;

public class MistralModelClient {

    private static final String API_URL = "https://api-inference.huggingface.co/models/mistralai/Mistral-Nemo-Instruct-2407";
    private static final String API_KEY = "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";  // Используйте свой API-ключ

    public static void main(String[] args) {
        try {
            // Создаем JSON для тела запроса, изменяем 'input' на 'inputs'
            String jsonRequestBody = "{\"inputs\": \"Каковы последние достижения в области искусственного интеллекта? Ответь мне на русском языке\"}";

            // Создание запроса
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                    .build();

            // Отправляем запрос и обрабатываем ответ
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Выводим ответ в консоль
            System.out.println("Ответ от API: " + response.body());

            // Проверка статуса ответа
            if (response.statusCode() != 200) {
                System.out.println("Ошибка API: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }
}
