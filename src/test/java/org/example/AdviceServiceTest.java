package org.example;

import org.example.model.Advice;
import org.example.model.User;
import org.example.repository.AdviceRepository;
import org.example.service.AdviceService;
import org.example.service.LLMClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdviceServiceTest {

    @Mock
    private AdviceRepository adviceRepository;

    @Mock
    private LLMClient llmClient;

    @InjectMocks
    private AdviceService adviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveAdvice_SavesAdviceWithCurrentDate() {
        // Arrange
        Advice advice = new Advice();
        advice.setAdviceText("Save more, spend less!");

        // Act
        adviceService.saveAdvice(advice);

        // Assert
        ArgumentCaptor<Advice> adviceCaptor = ArgumentCaptor.forClass(Advice.class);
        verify(adviceRepository).save(adviceCaptor.capture());
        Advice savedAdvice = adviceCaptor.getValue();

        assertNotNull(savedAdvice.getDateGenerated());
        assertEquals(LocalDate.now(), savedAdvice.getDateGenerated());
        assertEquals("Save more, spend less!", savedAdvice.getAdviceText());
    }

    @Test
    void getAdviceForUser_ReturnsAdvicesForGivenUser() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Advice advice1 = new Advice();
        advice1.setId(1L);
        advice1.setUser(user);
        advice1.setAdviceText("Advice 1");

        Advice advice2 = new Advice();
        advice2.setId(2L);
        advice2.setUser(user);
        advice2.setAdviceText("Advice 2");

        when(adviceRepository.findAllByUserId(user.getId())).thenReturn(List.of(advice1, advice2));

        // Act
        List<Advice> advices = adviceService.getAdviceForUser(user);

        // Assert
        assertEquals(2, advices.size());
        assertEquals("Advice 1", advices.get(0).getAdviceText());
        assertEquals("Advice 2", advices.get(1).getAdviceText());
        verify(adviceRepository, times(1)).findAllByUserId(user.getId());
    }

    @Test
    void generateAdviceFromLLM_ReturnsGeneratedAdvice() throws IOException, InterruptedException {
        // Arrange
        String prompt = "Give me some advice.";
        String expectedAdvice = "Spend wisely and save regularly.";
        when(llmClient.getAdviceFromModel(prompt)).thenReturn(expectedAdvice);

        // Act
        String generatedAdvice = adviceService.generateAdviceFromLLM(prompt);

        // Assert
        assertEquals(expectedAdvice, generatedAdvice);
        verify(llmClient, times(1)).getAdviceFromModel(prompt);
    }
}
