package org.example.controller;

import org.example.model.Advice;
import org.example.model.User;
import org.example.model.FinancialTransaction;
import org.example.service.AdviceService;
import org.example.service.FinancialTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class AdviceController {

    @Autowired
    private AdviceService adviceService;

    @Autowired
    private FinancialTransactionService transactionService;

    @GetMapping("/advice")
    public String viewAdvice(@AuthenticationPrincipal User user, Model model) {
        List<Advice> advices = adviceService.getAdviceForUser(user);
        model.addAttribute("advices", advices);
        return "advice"; // Страница с советами
    }

    @PostMapping("/advice/request")
    public String requestAdvice(@AuthenticationPrincipal User user, Model model) throws IOException, InterruptedException {
        // Получаем все транзакции пользователя
        List<FinancialTransaction> transactions = transactionService.getTransactionsByUserId(user.getId());

        // Формируем запрос для LLM
        StringBuilder prompt = new StringBuilder();
        prompt.append("Скажи совет или интересный факт на основе моих транзакций:");
        for (FinancialTransaction transaction : transactions) {
            prompt.append("Тип: ").append(transaction.getType()).append(", ")
                    .append("Сумма: ").append(transaction.getAmount()).append(", ")
                    .append("Категория: ").append(transaction.getCategory());
        }

        // Отправляем запрос в LLM модель и получаем ответ
        String generatedAdvice = adviceService.generateAdviceFromLLM(prompt.toString());

        // Сохраняем совет
        Advice advice = new Advice();
        advice.setUser(user);
        advice.setAdviceText(generatedAdvice);
        adviceService.saveAdvice(advice);

        // Добавляем совет в модель для отображения
        model.addAttribute("generatedAdvice", generatedAdvice);

        return "advice"; // Возвращаемся на страницу с советами
    }
}
