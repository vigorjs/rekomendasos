package com.virgo.todoapp.service;

import com.virgo.todoapp.utils.dto.TaskRequestDTO;
import com.virgo.todoapp.entity.meta.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    Task create(TaskRequestDTO req);
    Page<Task> getAll(Pageable pageable, String name);
    Task getById(Integer id);
    void delete(Integer id);
//    Task update (fitur)
    Task updateById(Integer id, TaskRequestDTO req);
}
