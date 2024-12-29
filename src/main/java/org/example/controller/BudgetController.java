package org.example.controller;

import org.example.model.Budget;
import org.example.model.User;
import org.example.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/budget")
    public String viewBudget(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("budget", budgetService.getBudgetByUser(user).orElse(null));
        return "budget";
    }

    @PostMapping("/budget")
    public String setBudget(@AuthenticationPrincipal User user, Budget budget) {
        budget.setUser(user); // Устанавливаем пользователя
        budgetService.setBudget(budget); // Используем обновленный метод из сервиса
        return "redirect:/budget"; // Перенаправляем на страницу бюджета
    }
}
