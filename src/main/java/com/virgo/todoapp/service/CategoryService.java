package com.virgo.todoapp.service;

import com.virgo.todoapp.entity.meta.Category;
import com.virgo.todoapp.utils.dto.CategoryRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Category create(CategoryRequestDTO req);
    Page<Category> getAll(Pageable pageable, String name);
    Category getById(Integer id);
    void delete(Integer id);
    Category updateById(Integer id, CategoryRequestDTO req);

}
