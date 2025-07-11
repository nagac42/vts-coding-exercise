package com.c42.poc.vts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record LoginRequest(

    @NotBlank(message = "UserId is required. Enter registered username or email")
    String userId,

    @NotBlank(message = "Password is required")
    String password
    ){}


