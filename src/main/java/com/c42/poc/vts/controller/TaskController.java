package com.c42.poc.vts.controller;

import com.c42.poc.vts.dto.*;
import com.c42.poc.vts.entity.Priority;
import com.c42.poc.vts.service.TaskService;
import com.c42.poc.vts.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    @Value("${pagination.default.page.size}")
    private String defaultPageSize;


    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<CreateTaskResponse> create(@Valid @RequestBody CreateTaskRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(request, userDetails.getUsername()));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<CreateTaskResponse> updateTaskById(@Valid @NotNull @PathVariable Long taskId, @Valid @RequestBody CreateTaskRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(taskService.updateTaskById(request, taskId, userDetails.getUsername()));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<GetTaskResponse> getTaskById(@Valid @NotNull @PathVariable Long taskId,
                                                         @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok().body(taskService.getTaskById(taskId, userDetails.getUsername()));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<CreateTaskResponse> deleteTaskById(@Valid @NotNull @PathVariable(required = true) Long taskId,
                                                       @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok().body(taskService.deleteTaskById(taskId, userDetails.getUsername()));
    }


    @GetMapping
    public ResponseEntity<Page<GetTaskResponse>> getUserTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dueBefore,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate,asc") String sort
    ) {
        Page<GetTaskResponse> tasks = taskService.getUserTasks(
                userDetails.getUsername(), priority, dueBefore, page, size, sort);
        return ResponseEntity.ok(tasks);
    }


}
