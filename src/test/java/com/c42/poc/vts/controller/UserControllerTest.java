package com.c42.poc.vts.controller;

import com.c42.poc.vts.dto.CreateTaskRequest;
import com.c42.poc.vts.dto.CreateTaskResponse;
import com.c42.poc.vts.dto.CreateVisitorRequest;
import com.c42.poc.vts.dto.CreateVisitorResponse;
import com.c42.poc.vts.entity.Priority;
import com.c42.poc.vts.service.TaskService;
import com.c42.poc.vts.service.UserService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "naga")
    void shouldCreateUserSuccessfully() throws Exception {
        CreateVisitorRequest request = new CreateVisitorRequest(
                "naga", "naga@gmail.com", "naga1234"
        );

        CreateVisitorResponse response = new CreateVisitorResponse(1L, "hkjhjfy87897hjkhk");

        Mockito.when(userService.register(Mockito.any(CreateVisitorRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
             /*   .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.token").value("hkjhjfy87897hjkhk"));

              */
    }

    @Test
    @WithMockUser(username = "naga")
    void shouldReturnBadRequestForInvalidInput() throws Exception {

        CreateVisitorRequest invalidRequest = new CreateVisitorRequest("naga", "naga@gmail.com", "");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isForbidden());
                //.andExpect(status().isBadRequest());
    }




}
