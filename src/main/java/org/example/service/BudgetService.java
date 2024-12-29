package org.example.service;

import org.example.model.Budget;
import org.example.model.User;
import org.example.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public Optional<Budget> getBudgetByUser(User user) {
        return budgetRepository.findByUserId(user.getId());
    }

    public void setBudget(Budget budget) {
        // Проверяем, существует ли бюджет для данного пользователя
        budgetRepository.findByUserId(budget.getUser().getId())
                .ifPresent(existingBudget -> budget.setId(existingBudget.getId())); // Если существует, обновляем ID
        budgetRepository.save(budget); // Сохраняем бюджет (обновляем или создаем новый)
    }
}

