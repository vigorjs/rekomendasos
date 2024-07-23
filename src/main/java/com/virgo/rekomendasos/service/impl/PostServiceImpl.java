package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.model.meta.Post;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.PlaceRepository;
import com.virgo.rekomendasos.repo.PostRepository;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.service.PlaceService;
import com.virgo.rekomendasos.service.PostService;
import com.virgo.rekomendasos.utils.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceService placeService;

    @Override
    @Transactional
    public Post create(PostDto obj) {
        User user = userRepository.findById(obj.getUser_id()).orElseThrow(() -> new RuntimeException("User not found"));
        Place place = placeRepository.findById(obj.getPlace_id()).orElse(null);

        if (place == null) {
            List<Place> places = placeService.getAllPlacesFromApi();
            for (Place pl : places) {
                if (pl.getId().equals(obj.getPlace_id())) {
                    place = pl;
                }
            }
        }

        placeService.create(place);

        return Post.builder()
                .title(obj.getTitle())
                .description(obj.getDescription())
                .picture(obj.getPicture())
                .star_review(obj.getStar_review())
                .user(user)
                .place(place)
                .build();
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post findById(Integer id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    @Transactional
    public Post update(Integer id, PostDto obj) {
        User user = userRepository.findById(obj.getUser_id()).orElseThrow(() -> new RuntimeException("User not found"));
        Place place = placeRepository.findById(obj.getPlace_id()).orElse(null);

        if (place == null) {
            List<Place> places = placeService.getAllPlacesFromApi();
            for (Place pl : places) {
                if (pl.getId().equals(obj.getPlace_id())) {
                    place = pl;
                }
            }
        }

        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setTitle(obj.getTitle());
        post.setDescription(obj.getDescription());
        post.setPicture(obj.getPicture());
        post.setStar_review(obj.getStar_review());
        post.setUser(user);
        post.setPlace(place);

        return postRepository.save(post);
    }

    @Override
    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post findByUser(Integer user_id, Integer id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

    }

    @Override
    public List<Post> findAllByUser(Integer user_id) {
        return postRepository.findAllByUserId(user_id);
    }

    @Override
    public Post createByUser(Integer user_id, PostDto obj) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        Place place = placeRepository.findById(obj.getPlace_id()).orElse(null);

        if (place == null) {
            List<Place> places = placeService.getAllPlacesFromApi();
            for (Place pl : places) {
                if (pl.getId().equals(obj.getPlace_id())) {
                    place = pl;
                }
            }
        }

        placeService.create(place);

        return Post.builder()
                .title(obj.getTitle())
                .description(obj.getDescription())
                .picture(obj.getPicture())
                .star_review(obj.getStar_review())
                .user(user)
                .place(place)
                .build();
    }

    @Override
    public Post updateByUser(Integer user_id, Integer id, PostDto obj) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        Place place = placeRepository.findById(obj.getPlace_id()).orElse(null);

        if (place == null) {
            List<Place> places = placeService.getAllPlacesFromApi();
            for (Place pl : places) {
                if (pl.getId().equals(obj.getPlace_id())) {
                    place = pl;
                }
            }
        }

        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setTitle(obj.getTitle());
        post.setDescription(obj.getDescription());
        post.setPicture(obj.getPicture());
        post.setStar_review(obj.getStar_review());
        post.setUser(user);
        post.setPlace(place);

        return postRepository.save(post);
    }

    @Override
    public void deleteByUser(Integer user_id, Integer id) {
        postRepository.deleteById(id);
    }
}
