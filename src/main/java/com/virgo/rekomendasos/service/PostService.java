package com.virgo.rekomendasos.service;


import com.virgo.rekomendasos.model.meta.Post;
import com.virgo.rekomendasos.utils.dto.PostDto;

import java.util.List;

public interface PostService {
    Post create(PostDto obj);

    List<Post> findAll();

    Post findById(Integer id);

    Post update(Integer id, PostDto obj);

    void deleteById(Integer id);

    Post findByUser(Integer user_id, Integer id);

    List<Post> findAllByUser(Integer user_id);

    Post createByUser(Integer user_id, PostDto obj);

    Post updateByUser(Integer user_id, Integer id, PostDto obj);

    void deleteByUser(Integer user_id, Integer id);
}