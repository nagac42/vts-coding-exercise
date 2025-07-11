package com.c42.poc.vts.controller;

import com.c42.poc.vts.dto.CreateVisitorRequest;
import com.c42.poc.vts.dto.CreateVisitorResponse;
import com.c42.poc.vts.dto.LoginRequest;
import com.c42.poc.vts.dto.LoginResponse;
import com.c42.poc.vts.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<CreateVisitorResponse> register(@Valid @RequestBody CreateVisitorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) throws AuthenticationException {
        return ResponseEntity.ok(userService.authenticate(request));
    }


}
