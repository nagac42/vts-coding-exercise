package com.c42.poc.vts.controller;

import com.c42.poc.vts.dto.CreateTaskRequest;
import com.c42.poc.vts.dto.CreateTaskResponse;
import com.c42.poc.vts.entity.Priority;
import com.c42.poc.vts.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "naga")
    void shouldCreateTaskSuccessfully() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest(
                "Task1", "Task1 Description", Priority.HIGH , LocalDateTime.now().plusDays(15)
        );

        CreateTaskResponse response = new CreateTaskResponse(1L, "Task created successfully");

        Mockito.when(taskService.create(Mockito.any(CreateTaskRequest.class), Mockito.eq("naga")))
                .thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // Required for POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId").value(1L))
                .andExpect(jsonPath("$.message").value("Task created successfully"));
    }



    @Test
    @WithMockUser(username = "raju")
    void shouldFailValidationForBlankTitle() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest(
                "", "Description", Priority.HIGH, LocalDateTime.now().plusDays(1)
        );

        mockMvc.perform(post("/api/tasks")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // Required for POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is required"));
    }


}
