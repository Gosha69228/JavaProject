package org.example.controller;

import org.example.model.*;
import org.example.service.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {

    private final ModeratorService moderatorService;

    @Autowired
    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    // Главная страница модератора со списком пользователей
    @GetMapping
    public String getModeratorPage(Model model) {
        List<User> users = moderatorService.getAllUsers();
        model.addAttribute("users", users);
        return "moderator";
    }

    // Страница профиля пользователя
    @GetMapping("/user/{userId}")
    public String getUserProfile(@PathVariable Long userId, Model model) {
        User user = moderatorService.getUserById(userId);
        List<FinancialTransaction> transactions = moderatorService.getUserTransactions(userId);
        Budget budget = moderatorService.getUserBudget(userId);

        model.addAttribute("user", user);
        model.addAttribute("transactions", transactions);
        model.addAttribute("budget", budget);
        return "user-profile";
    }

    // Создание отчета для пользователя
    @PostMapping("/user/{userId}/create-report")
    public String createReport(@PathVariable Long userId, @RequestParam String content) {
        moderatorService.createReport(userId, content);
        return "redirect:/moderator/user/" + userId;
    }
}