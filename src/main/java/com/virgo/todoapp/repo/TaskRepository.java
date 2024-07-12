package com.virgo.todoapp.repo;

import com.virgo.todoapp.entity.meta.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
