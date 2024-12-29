package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Для какого пользователя создан отчет

    @Column(nullable = false)
    private LocalDate generatedDate; // Дата создания отчета

    @Column(nullable = false, length = 2000)
    private String content; // Содержание отчета (например, текст или ссылка на файл)
}
