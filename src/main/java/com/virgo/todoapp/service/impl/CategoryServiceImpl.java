package com.virgo.todoapp.service.impl;

import com.virgo.todoapp.config.advisers.exception.NotFoundException;
import com.virgo.todoapp.entity.meta.Category;
import com.virgo.todoapp.entity.meta.User;
import com.virgo.todoapp.repo.CategoryRepository;
import com.virgo.todoapp.service.AuthenticationService;
import com.virgo.todoapp.service.CategoryService;
import com.virgo.todoapp.utils.dto.CategoryRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final AuthenticationService authService;
    private final CategoryRepository categoryRepository;

    @Override
    public Category create(CategoryRequestDTO req) {
        User user = authService.getUserAuthenticated();

        Category category = Category.builder()
                .name(req.getName())
                .user(user)
                .build();

        categoryRepository.save(category);
        return category;
    }

    @Override
    public Page<Category> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category getById(Integer id) {
        return categoryRepository.findById(id).orElseThrow( () -> new RuntimeException("Category not Found") );
    }

    @Override
    public void delete(Integer id) {
        if (categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
        }else {
            throw new NotFoundException("Category dengan ID " + id + "tidak ditemukan");
        }
    }

    @Override
    public Category updateById(Integer id, CategoryRequestDTO req) {
        Category category = getById(id);

        if (req.getName() != null && !req.getName().isEmpty()) {
            category.setName(req.getName());
        }

        return category;
    }
}
