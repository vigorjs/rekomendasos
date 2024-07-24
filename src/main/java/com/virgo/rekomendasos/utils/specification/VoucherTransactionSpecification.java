package com.virgo.rekomendasos.utils.specification;

//import com.virgo.rekomendasos.model.meta.Task;

import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import org.springframework.data.jpa.domain.Specification;

public class VoucherTransactionSpecification {

    public static Specification<VoucherTransaction> voucherByUser(User user) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
    }

    public static Specification<VoucherTransaction> hasName(String name) {
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

    public static Specification<VoucherTransaction> getSpecification(User user, String name) {
        return Specification.where(voucherByUser(user)).and(hasName(name));
    }
}

