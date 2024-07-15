package com.virgo.todoapp.repo;

import com.virgo.todoapp.entity.meta.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
