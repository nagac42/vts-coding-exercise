package com.c42.poc.vts.service;

import com.c42.poc.vts.dto.*;
import com.c42.poc.vts.entity.*;
import com.c42.poc.vts.exception.*;
import com.c42.poc.vts.mapper.TaskMapper;
import com.c42.poc.vts.repository.TaskRepository;
import com.c42.poc.vts.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private UserService userService;


    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("naga");
        user.setEmail("naga@gmail.com");

        task = new Task();
        task.setId(1L);
        task.setTitle("POC Task");
        task.setDescription("POC Task");
        task.setPriority(Priority.HIGH);
        task.setDueDate(LocalDate.now().plusDays(5).atStartOfDay());
        task.setUser(user);
    }

    @Test
    void testCreateTask() {
        CreateTaskRequest request = new CreateTaskRequest("Test Task", "Test Task desc", Priority.MEDIUM, LocalDate.now().plusDays(3).atStartOfDay());

        when(userService.getUser("naga", "naga")).thenReturn(user);
        when(taskMapper.toEntity(request)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);

        CreateTaskResponse response = taskService.create(request, "naga");

        assertEquals(task.getId(), response.taskId());
        assertEquals("Task created successfully", response.message());
    }

    @Test
    void testUpdateTask() {
        CreateTaskRequest updateRequest = new CreateTaskRequest("Task1 Updated", "New Description", Priority.HIGH, LocalDate.now().plusDays(5).atStartOfDay()
        );

        when(userService.getUser("naga", "naga")).thenReturn(user);
        when(taskRepository.findByIdAndUserId(task.getId(), user.getId())).thenReturn(Optional.of(task));
        when(taskRepository.saveAndFlush(any(Task.class))).thenReturn(task);

        CreateTaskResponse response = taskService.updateTaskById(updateRequest, task.getId(), "naga");

        assertEquals(task.getId(), response.taskId());
        assertEquals("Task updated successfully", response.message());
    }

    @Test
    void testGetTaskById() {
        when(userService.getUser("naga", "naga")).thenReturn(user);
        when(taskRepository.findByIdAndUserId(task.getId(), user.getId())).thenReturn(Optional.of(task));

        GetTaskResponse response = taskService.getTaskById(task.getId(), "naga");

        assertEquals(task.getId(), response.id());
        assertEquals(task.getTitle(), response.title());
    }

    @Test
    void testDeleteTask() {
        when(userService.getUser("naga", "naga")).thenReturn(user);
        when(taskRepository.findByIdAndUserId(task.getId(), user.getId())).thenReturn(Optional.of(task));

        CreateTaskResponse response = taskService.deleteTaskById(task.getId(), "naga");

        verify(taskRepository).deleteById(task.getId());
        assertEquals("Task deleted successfully", response.message());
    }

    @Test
    void testGetUserTasks() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title"));
        Page<Task> mockPage = new PageImpl<>(List.of(task));

        when(userRepository.findByUsernameOrEmail("naga", "naga")).thenReturn(Optional.of(user));
        when(taskRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(mockPage);

        Page<GetTaskResponse> response = taskService.getUserTasks(
                "naga", Priority.HIGH, LocalDate.now().plusDays(7), 0, 10, "title,asc");

        assertEquals(1, response.getTotalElements());
        assertEquals(task.getId(), response.getContent().get(0).id());
    }
}