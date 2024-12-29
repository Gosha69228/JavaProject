package org.example.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.model.Report;
import org.example.model.User;
import org.example.repository.ReportRepository;
import org.example.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class ReportController {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportController(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    // Метод для отображения страницы с отчетами текущего пользователя
    @GetMapping("/reports")
    public String getReportsPage(Model model) {
        // Получаем текущего авторизованного пользователя
        User currentUser = getCurrentUser();

        // Получаем отчеты только для текущего пользователя
        List<Report> reports = reportRepository.findAllByUserId(currentUser.getId());
        model.addAttribute("reports", reports);
        return "reports"; // Имя шаблона (reports.html)
    }

    // Метод для скачивания отчета в формате PDF
    @GetMapping("/reports/download/{id}")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) throws IOException, DocumentException {
        User currentUser = getCurrentUser();

        // Находим отчет по ID и проверяем, принадлежит ли он текущему пользователю
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (!report.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to access this report");
        }

        // Создаем документ PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        String fontPath = "./src/Pacifico.ttf"; // Укажите путь к вашему .ttf файлу
        BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        document.open();
        Font font = new Font(baseFont, 14, Font.NORMAL);// Поддержка кириллицы
        document.add(new Paragraph("Отчет от: " + report.getGeneratedDate() + "\n" + report.getContent(), font));
        document.close();



        // Формируем ResponseEntity для скачивания
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=document.pdf");
        headers.add("Content-Type", "application/pdf");

        return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
    }


// Метод для получения текущего авторизованного пользователя
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Получаем email текущего пользователя
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")); // Используем orElseThrow
    }
}