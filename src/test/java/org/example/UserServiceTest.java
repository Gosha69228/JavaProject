package org.example;

import org.example.model.User;
import org.example.model.enumc.Role;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        assertEquals("user1@example.com", users.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createUser_UserDoesNotExist_CreatesNewUser() {
        // Arrange
        User user = new User();
        user.setEmail("newuser@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Act
        boolean result = userService.createUser(user);

        // Assert
        assertTrue(result);
        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.isActive());
        assertEquals(Role.ROLE_USER, user.getRole());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("newuser@example.com", userCaptor.getValue().getEmail());
    }

    @Test
    void createUser_UserAlreadyExists_ReturnsFalse() {
        // Arrange
        User existingUser = new User();
        existingUser.setEmail("existing@example.com");

        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        User newUser = new User();
        newUser.setEmail("existing@example.com");

        // Act
        boolean result = userService.createUser(newUser);

        // Assert
        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUserById_UserExists_DeletesUser() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.deleteUserById(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }
}
