package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.FinancialTransaction;
import org.example.repository.FinancialTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinancialTransactionService {

    @Autowired
    private FinancialTransactionRepository transactionRepository;

    public void createTransaction(FinancialTransaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<FinancialTransaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findAllByUserId(userId);
    }
}
