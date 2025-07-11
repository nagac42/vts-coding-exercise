package com.c42.poc.vts.dto;

public record LoginResponse(
        Long userId,
        String token
){}
