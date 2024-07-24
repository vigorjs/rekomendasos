package com.virgo.rekomendasos.utils.specification;

import com.virgo.rekomendasos.model.meta.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

//    public static Specification<User> tasksByUser(User user) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
//    }

    public static Specification<User> hasName(String name) {
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

    public static Specification<User> getSpecification(String name) {
        return Specification.where(hasName(name));
    }

//    public static Specification<User> getSpecification(User user, String name) {
//        return Specification.where(tasksByUser(user)).and(hasName(name));
//    }
}

