package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "advices")
public class Advice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Для какого пользователя совет

    @Column(nullable = false, length = 500)
    private String adviceText; // Текст совета

    @Column(nullable = false)
    private LocalDate dateGenerated; // Дата создания совета
}
