package com.c42.poc.vts.dto;

import com.c42.poc.vts.entity.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


public record GetTaskResponse(

    Long id,

    String title,

    String description,

    Priority priority,

    LocalDateTime dueDate
    ){}


