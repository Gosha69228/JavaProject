package org.example.controller;

import org.example.model.User;
import org.example.model.enumc.Role;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Инжекция PasswordEncoder

    // Стартовая страница для администратора
    @GetMapping
    public String adminHome() {
        return "admin/admin-home"; // Ссылка на шаблон для стартовой страницы администратора
    }

    // Страница для управления пользователями
    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/manage-users"; // Ссылка на шаблон для управления пользователями
    }

    // Создание нового пользователя через форму
    @PostMapping("/users")
    public String createUser(@RequestParam String email, @RequestParam String name,
                             @RequestParam String password, @RequestParam String role, Model model) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);  // Шифруем пароль
        user.setActive(true);  // По умолчанию активен

        // Добавляем роль в зависимости от выбора
        try {
            Role userRole = Role.valueOf(role);  // Преобразуем строку в роль
            user.setRole(userRole);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Некорректная роль.");
            return "admin/manage-users";  // Возвращаем ошибку, если роль не существует
        }

        // Проверяем, существует ли пользователь с таким email
        if (!userService.createUser(user)) {
            model.addAttribute("error", "Пользователь с таким email уже существует.");
            return "admin/manage-users";  // Возвращаем ошибку, если email уже занят
        }

        return "redirect:/admin/users";  // Перенаправляем на страницу с пользователями
    }

    // Удаление пользователя по ID
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users"; // Перенаправление на список пользователей
    }
}
