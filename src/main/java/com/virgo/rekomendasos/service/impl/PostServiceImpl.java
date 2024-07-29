package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.model.meta.Post;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.PlaceRepository;
import com.virgo.rekomendasos.repo.PostRepository;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.service.AuthenticationService;
import com.virgo.rekomendasos.service.PlaceService;
import com.virgo.rekomendasos.service.PostService;
import com.virgo.rekomendasos.utils.dto.PostDto;
import com.virgo.rekomendasos.utils.dto.UserPostDto;
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

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    @Transactional
    public Post create(PostDto obj) {
        User user = userRepository.findById(obj.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Place place = placeRepository.findById(obj.getPlaceId()).orElse(null);

        if (place == null) {
            List<Place> places = placeService.getAllPlacesFromApi();
            for (Place pl : places) {
                if (pl.getId().equals(obj.getPlaceId())) {
                    place = pl;
                }
            }
        }

        if (place == null) {
            throw new RuntimeException("Place not found");
        }

        user.setPoint(user.getPoint() + 2);
        userRepository.save(user);

        place.setRating(place.getRating() + obj.getRating());
        placeRepository.save(place);

        Post post = Post.builder()
                .title(obj.getTitle())
                .description(obj.getDescription())
                .picture(obj.getPicture())
                .rating(obj.getRating())
                .user(user)
                .place(place)
                .build();

        return postRepository.save(post);
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
        User user = userRepository.findById(obj.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Place place = placeRepository.findById(obj.getPlaceId()).orElseThrow(() -> new RuntimeException("Place not found"));

        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        place.setRating(place.getRating() - post.getRating());
        place.setRating(place.getRating() + obj.getRating());
        placeRepository.save(place);

        post.setTitle(obj.getTitle());
        post.setDescription(obj.getDescription());
        post.setPicture(obj.getPicture());
        post.setRating(obj.getRating());
        post.setUser(user);
        post.setPlace(place);

        return postRepository.save(post);
    }

    @Override
    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post findByUser(Integer id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    public List<Post> findAllByUser() {
        User user = authenticationService.getUserAuthenticated();
        return user.getPosts();
    }

    @Override
    @Transactional
    public Post createByUser(UserPostDto obj) {
        User user = authenticationService.getUserAuthenticated();
        Place place = placeRepository.findById(obj.getPlaceId()).orElse(null);

        if (place == null) {
            List<Place> places = placeService.getAllPlacesFromApi();
            for (Place pl : places) {
                if (pl.getId().equals(obj.getPlaceId())) {
                    place = pl;
                }
            }
        }

        if (place == null) {
            throw new RuntimeException("Place not found");
        }

        user.setPoint(user.getPoint() + 2);
        userRepository.save(user);

        place.setRating(place.getRating() + obj.getRating());
        placeRepository.save(place);

        return Post.builder()
                .title(obj.getTitle())
                .description(obj.getDescription())
                .picture(obj.getPicture())
                .rating(obj.getRating())
                .user(user)
                .place(place)
                .build();
    }

    @Override
    @Transactional
    public Post updateByUser(Integer id, UserPostDto obj) {
        User user = authenticationService.getUserAuthenticated();
        Place place = placeRepository.findById(obj.getPlaceId()).orElse(null);

        if (place == null) {
            List<Place> places = placeService.getAllPlacesFromApi();
            for (Place pl : places) {
                if (pl.getId().equals(obj.getPlaceId())) {
                    place = pl;
                }
            }
        }

        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        place.setRating(place.getRating() - post.getRating());
        place.setRating(place.getRating() + obj.getRating());
        placeRepository.save(place);

        post.setTitle(obj.getTitle());
        post.setDescription(obj.getDescription());
        post.setPicture(obj.getPicture());
        post.setRating(obj.getRating());
        post.setUser(user);
        post.setPlace(place);

        return postRepository.save(post);
    }

    @Override
    public void deleteByUser(Integer id) {
        postRepository.deleteById(id);
    }
}
