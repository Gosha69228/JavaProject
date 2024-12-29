package org.example.configurations;

import lombok.RequiredArgsConstructor;
import org.example.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Включение CSRF защиты
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/registration", "/login", "/logout").permitAll()  // Разрешение публичных URL
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Доступ к админке только для администраторов
                        .requestMatchers("/moderator/**").hasRole("MODERATOR")  // Доступ для модераторов
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/advice/request").hasRole("USER")// Доступ для пользователей
                        .anyRequest().authenticated()  // Все остальные запросы требуют аутентификации
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Страница логина
                        .successHandler((request, response, authentication) -> {
                            var authorities = authentication.getAuthorities();
                            String redirectUrl = authorities.stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ? "/admin" :
                                    authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MODERATOR")) ? "/moderator" :
                                            "/home";
                            response.sendRedirect(redirectUrl);
                        })
                        .permitAll()  // Разрешить всем пользователям доступ к логину
                )
                .logout(logout -> logout
                        .permitAll()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);  // Указание уровня энкода для BCrypt
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManager.class);
    }
}

