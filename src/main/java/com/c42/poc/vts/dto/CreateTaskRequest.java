package com.c42.poc.vts.dto;

import com.c42.poc.vts.entity.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class CreateTaskRequest{
    @NotBlank(message = "Title is required")
    String title;

    String description;

    @NotNull(message = "Priority is required and must be set to either HIGH, MEDIUM OR LOW")
    Priority priority;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dueDate;

}


