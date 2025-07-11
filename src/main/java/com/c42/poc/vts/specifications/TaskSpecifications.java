package com.c42.poc.vts.specifications;

import com.c42.poc.vts.entity.Priority;
import com.c42.poc.vts.entity.Task;
import com.c42.poc.vts.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TaskSpecifications {

    public static Specification<Task> byUser(User user) {
        return (root, query, cb) -> cb.equal(root.get("user"), user);
    }

    public static Specification<Task> byPriority(Priority priority) {
        return (root, query, cb) -> cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> dueBefore(LocalDate date) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dueDate"), date.atStartOfDay());
    }
}
