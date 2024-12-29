package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Для какого пользователя бюджет

    @Column(nullable = false)
    private Double totalIncome; // Планируемый доход

    @Column(nullable = false)
    private Double totalExpense; // Планируемые расходы

    @Column(nullable = false)
    private LocalDate startDate; // Начало периода

    @Column(nullable = false)
    private LocalDate endDate; // Конец периода
}
