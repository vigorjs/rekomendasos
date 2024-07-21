package com.virgo.todoapp.service.impl;

import com.virgo.todoapp.entity.meta.Category;
import com.virgo.todoapp.service.AuthenticationService;
import com.virgo.todoapp.service.CategoryService;
import com.virgo.todoapp.utils.dto.TaskRequestDTO;
import com.virgo.todoapp.entity.meta.Task;
import com.virgo.todoapp.entity.meta.User;
import com.virgo.todoapp.entity.enums.TaskStatus;
import com.virgo.todoapp.config.advisers.exception.NotFoundException;
import com.virgo.todoapp.repo.TaskRepository;
import com.virgo.todoapp.service.TaskService;
import com.virgo.todoapp.utils.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final AuthenticationService authService;
    private final CategoryService categoryService;

    @Override
    public Task create(TaskRequestDTO req) {
        User user = authService.getUserAuthenticated();

        Task task = Task.builder()
                .name(req.getName())
                .createdAt(Date.from(Instant.now()))
                .date(req.getDate())
                .deadline(req.getDeadline())
                .status(TaskStatus.ON_PROGRESS)
                .user(user)
                .build();

        if (req.getCategory() != null) {
            task.setCategory(categoryService.getById(req.getCategory()));
        }

        taskRepository.save(task);
        return task;
    }

    @Override
    public Page<Task> getAll(Pageable pageable, String name) {
        User currentUser = authService.getUserAuthenticated();
        Specification<Task> spec = TaskSpecification.getSpecification(currentUser, name);;
        return taskRepository.findAll(spec, pageable);
    }

    @Override
    public Task getById(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow( () -> new RuntimeException("Task Not Found"));

        if (!Objects.equals(task.getUser(), authService.getUserAuthenticated())){
            throw new RuntimeException("User not valid");
        }

        return task;
    }

    @Override
    public void delete(Integer id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new NotFoundException("Task dengan ID " + id + " tidak ditemukan");
        }
    }

    @Override
    public Task updateById(Integer id, TaskRequestDTO req){
        Task task = getById(id);

        if (req.getName() != null && !req.getName().isEmpty()) {
            task.setName(req.getName());
        }
        if (req.getDate() != null) {
            task.setDate(req.getDate());
        }
        if (req.getDeadline() != null) {
            task.setDeadline(req.getDeadline());
        }
        if (req.getStatus() != null) {
            task.setStatus(TaskStatus.valueOf(req.getStatus().toString()));
        }
        if (req.getCategory() != null) {
            task.setCategory(categoryService.getById(req.getCategory()));
        }

        return taskRepository.save(task);
    }

}
