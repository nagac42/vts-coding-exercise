package com.c42.poc.vts.repository;

import com.c42.poc.vts.entity.Task;
import com.c42.poc.vts.entity.Priority;
import com.c42.poc.vts.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setUsername("naga");
        testUser.setEmail("naga@example.com");
        testUser.setPassword("naga12345");
        testUser.setIsActive(true);
        testUser.setCreatedBy("TestVTS");
        testUser.setCreatedDate(LocalDateTime.now());
        testUser.setUpdatedBy("TestVTS");
        userRepository.save(testUser);
    }

    @Test
    void shouldSaveAndFindTaskByIdAndUserId() {
        Task task = new Task();
        task.setTitle("POC Test Task");
        task.setDescription("Test POC Description");
        task.setPriority(Priority.HIGH);
        task.setDueDate(LocalDateTime.now().plusDays(10));
        task.setUser(testUser);
        task.setCreatedBy("TestVTS");
        task.setCreatedDate(LocalDateTime.now());
        task.setUpdatedBy("TestVTS");

        Task saved = taskRepository.save(task);

        Optional<Task> found = taskRepository.findByIdAndUserId(saved.getId(), testUser.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("POC Test Task");
    }

    @Test
    void shouldReturnEmptyIfTaskDoesNotBelongToUser() {
        Task task = new Task();
        task.setTitle("Task200");
        task.setUser(testUser);
        task.setCreatedBy("TestVTS");
        task.setCreatedDate(LocalDateTime.now());
        task.setUpdatedBy("TestVTS");
        task.setPriority(Priority.LOW);
        taskRepository.save(task);

        Optional<Task> result = taskRepository.findByIdAndUserId(task.getId(), 200L); // wrong user ID

        assertThat(result).isEmpty();
    }
}