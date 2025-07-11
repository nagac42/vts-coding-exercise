package com.c42.poc.vts.service;

import com.c42.poc.vts.dto.CreateVisitorRequest;
import com.c42.poc.vts.dto.CreateVisitorResponse;
import com.c42.poc.vts.dto.LoginRequest;
import com.c42.poc.vts.dto.LoginResponse;
import com.c42.poc.vts.entity.User;
import com.c42.poc.vts.mapper.UserMapper;
import com.c42.poc.vts.repository.UserRepository;
import com.c42.poc.vts.security.JwtUtils;
import javax.naming.AuthenticationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CreateVisitorRequest request;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new CreateVisitorRequest("naga", "naga@gmail.com", "naga1234");

        user = new User();
        user.setId(1L);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword("hashed-password");
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        CreateVisitorRequest request = new CreateVisitorRequest("naga", "naga@gmail.com", "Password123");
        when(userRepository.findByUsernameOrEmail(request.getUsername(),request.getEmail()))
                .thenReturn(Optional.empty());
        when(userMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed-password");
        when(userRepository.save(user)).thenReturn(user);
        when(jwtUtils.generateToken(user)).thenReturn("775gjhghjf786");

        CreateVisitorResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals(1L, response.userId());
        assertEquals("775gjhghjf786", response.token());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        CreateVisitorRequest request = new CreateVisitorRequest("naga", "naga@gmail.com", "Password123");
        when(userRepository.findByUsernameOrEmail(request.getUsername(),request.getEmail()))
                .thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(request);
        });

        assertTrue(ex.getMessage().contains("Invalid request. Username or email already exists"));
    }

    @Test
    void shouldAuthenticateUserSuccessfully() throws AuthenticationException {
        LoginRequest loginRequest = new LoginRequest("naga", "Password123");

        when(userRepository.findByUsernameOrEmail("naga", "naga"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123", "hashed-password"))
                .thenReturn(true);
        when(jwtUtils.generateToken(user))
                .thenReturn("775gjhghjf786");

        LoginResponse response = userService.authenticate(loginRequest);

        assertEquals(user.getId(), response.userId());
        assertEquals("775gjhghjf786", response.token());
    }

    @Test
    void shouldFailAuthenticationForInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("naga", "Password");

        when(userRepository.findByUsernameOrEmail("naga", "naga"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password", "775gjhghjf786"))
                .thenReturn(false);

        assertThrows(AuthenticationException.class, () -> userService.authenticate(loginRequest));
    }

}