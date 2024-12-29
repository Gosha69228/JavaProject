package org.example.service;

import org.example.model.Advice;
import org.example.model.User;
import org.example.repository.AdviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class AdviceService {

    @Autowired
    private AdviceRepository adviceRepository;

    @Autowired
    private LLMClient llmClient; // Класс для общения с LLM моделью

    public void saveAdvice(Advice advice) {
        advice.setDateGenerated(LocalDate.now());
        adviceRepository.save(advice);
    }

    public List<Advice> getAdviceForUser(User user) {
        return adviceRepository.findAllByUserId(user.getId());
    }

    public String generateAdviceFromLLM(String prompt) throws IOException, InterruptedException {
        return llmClient.getAdviceFromModel(prompt);
    }
}
