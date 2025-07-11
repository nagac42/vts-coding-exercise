package com.c42.poc.vts.service;

import com.c42.poc.vts.dto.*;
import com.c42.poc.vts.entity.Priority;
import com.c42.poc.vts.entity.Task;
import com.c42.poc.vts.entity.User;
import com.c42.poc.vts.exception.TaskNotFoundException;
import com.c42.poc.vts.exception.UserNotFoundException;
import com.c42.poc.vts.mapper.TaskMapper;
import com.c42.poc.vts.mapper.UserMapper;
import com.c42.poc.vts.repository.TaskRepository;
import com.c42.poc.vts.repository.UserRepository;
import com.c42.poc.vts.security.JwtUtils;
import com.c42.poc.vts.specifications.TaskSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    private final UserService userService;

    private final TaskMapper taskMapper;

    public CreateTaskResponse create(CreateTaskRequest request, String username) {

        User user = userService.getUser(username,username);

        Task task = taskMapper.toEntity(request);
        task.setUser(user);

        Task saved = taskRepository.save(task);

        logger.info("User {} created Task {} successfully", username, saved.getId());

        return new CreateTaskResponse(saved.getId(), "Task created successfully");
    }

    public CreateTaskResponse updateTaskById(CreateTaskRequest request, Long taskId, String username) {

        User user = userService.getUser(username,username);

        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found or unauthorized"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setPriority(request.getPriority());

        Task saved = taskRepository.saveAndFlush(task);

        logger.info("Task {} updated successfully", taskId);

        return new CreateTaskResponse(saved.getId(), "Task updated successfully");
    }

    public GetTaskResponse getTaskById(Long taskId, String username) {

        User user = userService.getUser(username,username);

        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found or unauthorized"));


        return new GetTaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getPriority() , task.getDueDate() );

    }

    public CreateTaskResponse deleteTaskById(Long taskId, String username) {

        User user = userService.getUser(username,username);

        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found or unauthorized"));

        taskRepository.deleteById(taskId);
        logger.info("Task {} deleted successfully", taskId);
        return new CreateTaskResponse(taskId,"Task deleted successfully" );

    }


    public Page<GetTaskResponse> getUserTasks(String username, Priority priority, LocalDate dueBefore, int page, int size, String sort) {

        User user = userService.getUser(username,username);

        Sort sorting = Sort.by(sort.split(",")[0]);
        if (sort.endsWith(",desc")) sorting = sorting.descending();

        Pageable pageable = PageRequest.of(page, size, sorting);

        Specification<Task> spec = Specification.where(TaskSpecifications.byUser(user));

        if (priority != null) spec = spec.and(TaskSpecifications.byPriority(priority));
        if (dueBefore != null) spec = spec.and(TaskSpecifications.dueBefore(dueBefore));

        Page<Task> taskPage = taskRepository.findAll(spec, pageable);

        return taskPage.map(task -> new GetTaskResponse(
                task.getId(), task.getTitle(), task.getDescription(),
                task.getPriority(), task.getDueDate()));
    }






}



