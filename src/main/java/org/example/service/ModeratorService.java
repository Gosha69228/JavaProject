package org.example.service;

import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ModeratorService {

    private final UserRepository userRepository;
    private final FinancialTransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final ReportRepository reportRepository;

    @Autowired
    public ModeratorService(UserRepository userRepository,
                            FinancialTransactionRepository transactionRepository,
                            BudgetRepository budgetRepository,
                            ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.reportRepository = reportRepository;
    }

    // Получить список всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Получить данные пользователя по ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Получить все транзакции пользователя
    public List<FinancialTransaction> getUserTransactions(Long userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    // Получить бюджет пользователя
    public Budget getUserBudget(Long userId) {
        return budgetRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
    }

    // Создать отчет для пользователя
    public Report createReport(Long userId, String content) {
        User user = getUserById(userId);
        Report report = new Report();
        report.setUser(user);
        report.setGeneratedDate(LocalDate.now());
        report.setContent(content);
        return reportRepository.save(report);
    }
}