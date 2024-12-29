package org.example;

import org.example.model.FinancialTransaction;
import org.example.model.User;
import org.example.repository.FinancialTransactionRepository;
import org.example.service.FinancialTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FinancialTransactionServiceTest {

    @Mock
    private FinancialTransactionRepository transactionRepository;

    @InjectMocks
    private FinancialTransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_SavesTransaction() {
        // Arrange
        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setId(1L);
        transaction.setType("INCOME");
        transaction.setAmount(1000.0);
        transaction.setCategory("Salary");
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Monthly salary");

        // Act
        transactionService.createTransaction(transaction);

        // Assert
        ArgumentCaptor<FinancialTransaction> transactionCaptor = ArgumentCaptor.forClass(FinancialTransaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        FinancialTransaction savedTransaction = transactionCaptor.getValue();

        assertEquals("INCOME", savedTransaction.getType());
        assertEquals(1000.0, savedTransaction.getAmount());
        assertEquals("Salary", savedTransaction.getCategory());
    }

    @Test
    void getTransactionsByUserId_ReturnsUserTransactions() {
        // Arrange
        User user = new User();
        user.setId(1L);

        FinancialTransaction transaction1 = new FinancialTransaction();
        transaction1.setId(1L);
        transaction1.setUser(user);
        transaction1.setType("INCOME");
        transaction1.setAmount(1000.0);

        FinancialTransaction transaction2 = new FinancialTransaction();
        transaction2.setId(2L);
        transaction2.setUser(user);
        transaction2.setType("EXPENSE");
        transaction2.setAmount(500.0);

        when(transactionRepository.findAllByUserId(user.getId())).thenReturn(List.of(transaction1, transaction2));

        // Act
        List<FinancialTransaction> transactions = transactionService.getTransactionsByUserId(user.getId());

        // Assert
        assertEquals(2, transactions.size());
        assertEquals(1L, transactions.get(0).getId());
        assertEquals(2L, transactions.get(1).getId());
        verify(transactionRepository, times(1)).findAllByUserId(user.getId());
    }
}
