package com.virgo.todoapp.service;

import com.virgo.todoapp.utils.dto.TaskRequestDTO;
import com.virgo.todoapp.entity.meta.Task;
import com.virgo.todoapp.utils.response.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface TaskService {
    Task create(TaskRequestDTO req);
    Page<Task> getAll(Pageable pageable);
    Task getById(Integer id);
    void delete(Integer id);

//    Task update (fitur)
}
