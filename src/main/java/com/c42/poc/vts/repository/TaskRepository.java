package com.c42.poc.vts.repository;

import com.c42.poc.vts.entity.Task;
import com.c42.poc.vts.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> , JpaSpecificationExecutor<Task> {

    Optional<Task> findByIdAndUserId(Long taskId, Long userId);
}
