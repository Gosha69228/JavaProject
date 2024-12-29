package org.example;

import org.example.model.Budget;
import org.example.model.User;
import org.example.repository.BudgetRepository;
import org.example.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBudgetByUser_BudgetExists_ReturnsBudget() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Budget budget = new Budget();
        budget.setId(10L);
        budget.setUser(user);

        when(budgetRepository.findByUserId(user.getId())).thenReturn(Optional.of(budget));

        // Act
        Optional<Budget> result = budgetService.getBudgetByUser(user);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getId());
        verify(budgetRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    void getBudgetByUser_BudgetDoesNotExist_ReturnsEmpty() {
        // Arrange
        User user = new User();
        user.setId(1L);

        when(budgetRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        // Act
        Optional<Budget> result = budgetService.getBudgetByUser(user);

        // Assert
        assertFalse(result.isPresent());
        verify(budgetRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    void setBudget_BudgetExists_UpdatesBudget() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Budget existingBudget = new Budget();
        existingBudget.setId(10L);
        existingBudget.setUser(user);

        Budget newBudget = new Budget();
        newBudget.setUser(user);
        newBudget.setTotalIncome(5000.0);
        newBudget.setTotalExpense(2000.0);
        newBudget.setStartDate(LocalDate.now());
        newBudget.setEndDate(LocalDate.now().plusMonths(1));

        when(budgetRepository.findByUserId(user.getId())).thenReturn(Optional.of(existingBudget));

        // Act
        budgetService.setBudget(newBudget);

        // Assert
        assertEquals(10L, newBudget.getId());
        ArgumentCaptor<Budget> budgetCaptor = ArgumentCaptor.forClass(Budget.class);
        verify(budgetRepository).save(budgetCaptor.capture());
        Budget savedBudget = budgetCaptor.getValue();

        assertEquals(5000.0, savedBudget.getTotalIncome());
        assertEquals(2000.0, savedBudget.getTotalExpense());
        verify(budgetRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    void setBudget_BudgetDoesNotExist_CreatesNewBudget() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Budget newBudget = new Budget();
        newBudget.setUser(user);
        newBudget.setTotalIncome(5000.0);
        newBudget.setTotalExpense(2000.0);
        newBudget.setStartDate(LocalDate.now());
        newBudget.setEndDate(LocalDate.now().plusMonths(1));

        when(budgetRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        // Act
        budgetService.setBudget(newBudget);

        // Assert
        assertNull(newBudget.getId()); // ID устанавливается репозиторием
        ArgumentCaptor<Budget> budgetCaptor = ArgumentCaptor.forClass(Budget.class);
        verify(budgetRepository).save(budgetCaptor.capture());
        Budget savedBudget = budgetCaptor.getValue();

        assertEquals(5000.0, savedBudget.getTotalIncome());
        assertEquals(2000.0, savedBudget.getTotalExpense());
        verify(budgetRepository, times(1)).findByUserId(user.getId());
    }
}
