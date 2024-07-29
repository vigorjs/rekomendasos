package com.virgo.rekomendasos.repository;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.model.meta.Post;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.PlaceRepository;
import com.virgo.rekomendasos.repo.PostRepository;
import com.virgo.rekomendasos.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenCreatePost_thenReturnCreatedPost() {
        User user = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user);
        Place place = Place.builder()
                .id("satu")
                .name("Ragunan")
                .rating(8)
                .latitude(1242.23)
                .longitude(2374.32)
                .posts(new ArrayList<Post>())
                .build();
        placeRepository.save(place);
        Post post = Post.builder()
                .title("Main Yok")
                .user(user)
                .place(place)
                .description("deskripsi")
                .rating(10)
                .picture("kosong")
                .build();
        postRepository.save(post);

        Post foundPost = postRepository.findById(post.getId()).orElse(null);

        assertThat(foundPost).isNotNull();
    }

    @Test
    public void whenGetAllPosts_thenReturnAllPosts() {
        User user = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user);
        Place place = Place.builder()
                .id("satu")
                .name("Ragunan")
                .rating(8)
                .latitude(1242.23)
                .longitude(2374.32)
                .posts(new ArrayList<Post>())
                .build();
        placeRepository.save(place);
        Post post = Post.builder()
                .title("Main Yok")
                .user(user)
                .place(place)
                .description("deskripsi")
                .rating(10)
                .picture("kosong")
                .build();
        postRepository.save(post);
        User user2 = User.builder()
                .firstname("Enigma 2")
                .lastname("Camp")
                .email("user2@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user2);
        Place place2 = Place.builder()
                .id("dua")
                .name("Ragunan 2")
                .rating(8)
                .latitude(1242.23)
                .longitude(2374.32)
                .posts(new ArrayList<Post>())
                .build();
        placeRepository.save(place2);
        Post post2 = Post.builder()
                .title("Main Yok 2")
                .user(user2)
                .place(place2)
                .description("deskripsi")
                .rating(10)
                .picture("kosong")
                .build();
        postRepository.save(post2);

        List<Post> posts = postRepository.findAll();

        assertThat(posts).isNotNull();
        assertThat(posts).hasSize(2);
    }

    @Test
    public void whenGetPostById_thenReturnPost() {
        User user = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user);
        Place place = Place.builder()
                .id("satu")
                .name("Ragunan")
                .rating(8)
                .latitude(1242.23)
                .longitude(2374.32)
                .posts(new ArrayList<Post>())
                .build();
        placeRepository.save(place);
        Post post = Post.builder()
                .title("Main Yok")
                .user(user)
                .place(place)
                .description("deskripsi")
                .rating(10)
                .picture("kosong")
                .build();
        postRepository.save(post);

        Post foundPost = postRepository.findById(post.getId()).orElse(null);

        assertThat(foundPost).isNotNull();
    }

    @Test
    public void whenUpdatePostById_thenReturnUpdatedPost() {
        User user = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user);
        Place place = Place.builder()
                .id("satu")
                .name("Ragunan")
                .rating(8)
                .latitude(1242.23)
                .longitude(2374.32)
                .posts(new ArrayList<Post>())
                .build();
        placeRepository.save(place);
        Post post = Post.builder()
                .title("Main Yok")
                .user(user)
                .place(place)
                .description("deskripsi")
                .rating(10)
                .picture("kosong")
                .build();
        postRepository.save(post);

        Post foundPost = postRepository.findById(post.getId()).orElse(null);
        assertThat(foundPost).isNotNull();

        foundPost.setTitle("Updated Post");
        Post updatedPost = postRepository.save(foundPost);

        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getTitle()).isEqualTo("Updated Post");
    }

    @Test
    public void whenDeletePostById_thenPostShouldBeDeleted() {
        User user = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user);
        Place place = Place.builder()
                .id("satu")
                .name("Ragunan")
                .rating(8)
                .latitude(1242.23)
                .longitude(2374.32)
                .posts(new ArrayList<Post>())
                .build();
        placeRepository.save(place);
        Post post = Post.builder()
                .title("Main Yok")
                .user(user)
                .place(place)
                .description("deskripsi")
                .rating(10)
                .picture("kosong")
                .build();
        postRepository.save(post);

        postRepository.deleteById(post.getId());
        Post foundPost = postRepository.findById(post.getId()).orElse(null);

        assertThat(foundPost).isNull();
    }
}
