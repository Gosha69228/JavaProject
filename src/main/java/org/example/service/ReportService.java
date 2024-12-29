package org.example.service;

import org.example.model.Report;
import org.example.model.User;
import org.example.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public void generateReport(User user, String content) {
        Report report = new Report();
        report.setUser(user);
        report.setGeneratedDate(LocalDate.now());
        report.setContent(content);
        reportRepository.save(report);
    }

    public List<Report> getReportsForUser(User user) {
        return reportRepository.findAllByUserId(user.getId());
    }
}
