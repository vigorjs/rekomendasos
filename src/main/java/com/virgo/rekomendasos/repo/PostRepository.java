package com.virgo.rekomendasos.repo;

import com.virgo.rekomendasos.model.meta.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT * FROM post WHERE user_id = :userId", nativeQuery = true)
    List<Post> findAllByUserId(@Param("userId") Integer userId);
}
