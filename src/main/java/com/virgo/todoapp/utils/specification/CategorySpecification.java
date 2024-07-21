package com.virgo.todoapp.utils.specification;

import com.virgo.todoapp.entity.meta.Category;
import com.virgo.todoapp.entity.meta.User;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> tasksByUser(User user) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
    }

    public static Specification<Category> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name != null && !name.isBlank()) {
                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static Specification<Category> getSpecification(User user, String name) {
        return Specification.where(tasksByUser(user)).and(hasName(name));
    }
}
