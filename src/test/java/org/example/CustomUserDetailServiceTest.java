package org.example;

import org.example.model.User;
import org.example.model.enumc.Role;
import org.example.repository.UserRepository;
import org.example.service.CustomUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setRole(Role.ROLE_USER);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        User loadedUser = (User) customUserDetailService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(loadedUser);
        assertEquals("test@example.com", loadedUser.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_UserNotExists_ThrowsException() {
        // Arrange
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailService.loadUserByUsername("notfound@example.com"));
        assertEquals("User not found with email: notfound@example.com", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }
}
