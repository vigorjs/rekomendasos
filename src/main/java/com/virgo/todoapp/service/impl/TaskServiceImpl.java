package com.virgo.todoapp.service.impl;

import com.virgo.todoapp.utils.advisers.exception.AuthenticationException;
import com.virgo.todoapp.utils.dto.TaskRequestDTO;
import com.virgo.todoapp.entity.meta.Task;
import com.virgo.todoapp.entity.meta.User;
import com.virgo.todoapp.entity.enums.TaskStatus;
import com.virgo.todoapp.utils.advisers.exception.NotFoundException;
import com.virgo.todoapp.repo.TaskRepository;
import com.virgo.todoapp.repo.UserRepository;
import com.virgo.todoapp.service.TaskService;
import com.virgo.todoapp.utils.response.WebResponse;
import com.virgo.todoapp.utils.response.WebResponseError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Task create(TaskRequestDTO req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new AuthenticationException("Unauthorized, silahkan login"));

        Task task = Task.builder()
                .name(req.getName())
                .createdAt(Date.from(Instant.now()))
                .deadline(req.getDeadline())
                .status(TaskStatus.ON_PROGRESS)
                .user(user)
                .build();
        taskRepository.save(task);
        return task;
    }

    @Override
    public Page<Task> getAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Task getById(Integer id) {
        return taskRepository.findById(id).orElseThrow( () -> new RuntimeException("Task Not Found"));
    }

    @Override
    public void delete(Integer id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new NotFoundException("Task dengan ID " + id + " tidak ditemukan");
        }
    }

}
