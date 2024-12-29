package org.example.controller;

import org.springframework.ui.Model;
import org.example.model.FinancialTransaction;
import org.example.model.User;
import org.example.service.FinancialTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class FinancialTransactionController {

    @Autowired
    private FinancialTransactionService transactionService;

    @GetMapping("/transactions")
    public String viewTransactions(@AuthenticationPrincipal User user, Model model) {
        // Получаем транзакции текущего пользователя
        List<FinancialTransaction> transactions = transactionService.getTransactionsByUserId(user.getId());
        model.addAttribute("transactions", transactions);
        return "transactions"; // Отображаем страницу
    }

    @PostMapping("/transactions/new")
    public String createTransaction(@AuthenticationPrincipal User user, FinancialTransaction transaction) {
        // Устанавливаем владельца транзакции
        transaction.setUser(user);
        transactionService.createTransaction(transaction);
        return "redirect:/transactions"; // Перенаправление обратно на список транзакций
    }
}
