package org.example;

import org.example.model.*;
import org.example.repository.*;
import org.example.service.ModeratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModeratorServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FinancialTransactionRepository transactionRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ModeratorService moderatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<User> users = moderatorService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ReturnsUserIfExists() {
        // Arrange
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = moderatorService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_ThrowsExceptionIfUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> moderatorService.getUserById(1L));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserTransactions_ReturnsTransactions() {
        // Arrange
        FinancialTransaction transaction1 = new FinancialTransaction();
        transaction1.setId(1L);
        FinancialTransaction transaction2 = new FinancialTransaction();
        transaction2.setId(2L);

        when(transactionRepository.findAllByUserId(1L)).thenReturn(List.of(transaction1, transaction2));

        // Act
        List<FinancialTransaction> transactions = moderatorService.getUserTransactions(1L);

        // Assert
        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findAllByUserId(1L);
    }

    @Test
    void getUserBudget_ReturnsBudgetIfExists() {
        // Arrange
        Budget budget = new Budget();
        budget.setId(1L);

        when(budgetRepository.findByUserId(1L)).thenReturn(Optional.of(budget));

        // Act
        Budget result = moderatorService.getUserBudget(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(budgetRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getUserBudget_ThrowsExceptionIfBudgetNotFound() {
        // Arrange
        when(budgetRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> moderatorService.getUserBudget(1L));
        assertEquals("Budget not found", exception.getMessage());
        verify(budgetRepository, times(1)).findByUserId(1L);
    }

    @Test
    void createReport_SavesReport() {
        // Arrange
        User user = new User();
        user.setId(1L);
        String content = "Test report content";

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Report report = moderatorService.createReport(1L, content);

        // Assert
        assertNotNull(report);
        assertEquals(user, report.getUser());
        assertEquals(content, report.getContent());
        assertEquals(LocalDate.now(), report.getGeneratedDate());

        ArgumentCaptor<Report> reportCaptor = ArgumentCaptor.forClass(Report.class);
        verify(reportRepository).save(reportCaptor.capture());
        Report savedReport = reportCaptor.getValue();

        assertEquals(content, savedReport.getContent());
    }
}
