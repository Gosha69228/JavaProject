package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "financial_transactions")
public class FinancialTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Владелец операции

    @Column(nullable = false)
    private String type; // 'INCOME' или 'EXPENSE'

    @Column(nullable = false)
    private Double amount; // Сумма

    @Column(nullable = false)
    private String category; // Категория (например, "Еда", "Транспорт")

    @Column(nullable = false)
    private LocalDate date; // Дата операции

    private String description; // Описание
}
