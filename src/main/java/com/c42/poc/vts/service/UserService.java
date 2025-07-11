package com.c42.poc.vts.service;

import com.c42.poc.vts.dto.CreateVisitorRequest;
import com.c42.poc.vts.dto.CreateVisitorResponse;
import com.c42.poc.vts.dto.LoginRequest;
import com.c42.poc.vts.dto.LoginResponse;
import com.c42.poc.vts.entity.User;
import com.c42.poc.vts.exception.UserNotFoundException;
import com.c42.poc.vts.mapper.UserMapper;
import com.c42.poc.vts.repository.UserRepository;
import com.c42.poc.vts.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final UserMapper userMapper;

    public CreateVisitorResponse register(CreateVisitorRequest request) {
        boolean alreadyExists = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail()).isPresent();
        if (alreadyExists) {
            throw new IllegalArgumentException("Invalid request. Username or email already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);
        String token = jwtUtils.generateToken(saved);

        logger.info("User {} created successfully", saved.getId());

        return new CreateVisitorResponse(saved.getId(), token);
    }

    public LoginResponse authenticate(LoginRequest request) throws AuthenticationException {

        User user = getUser(request.userId(),request.userId());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthenticationException("Invalid Credentials");
        }

        String token = jwtUtils.generateToken(user);
        return new LoginResponse(user.getId(), token);

    }

    protected User getUser(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}

