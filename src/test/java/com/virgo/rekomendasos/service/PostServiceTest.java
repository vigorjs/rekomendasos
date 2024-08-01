package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.model.meta.Post;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.PlaceRepository;
import com.virgo.rekomendasos.repo.PostRepository;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.service.impl.PostServiceImpl;
import com.virgo.rekomendasos.utils.dto.PostDto;
import com.virgo.rekomendasos.utils.dto.UserPostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private PlaceService placeService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private GeoApiService geoApiService;

    @InjectMocks
    private PostServiceImpl postServiceImpl;

    private PostDto postDto;
    private UserPostDto userPostDto;
    private User user;
    private Place place;
    private Post post;

    @BeforeEach
    void setUp() {
        postDto = new PostDto();
        postDto.setUserId(1);
        postDto.setPlaceId("place1");
        postDto.setTitle("Test Post");
        postDto.setDescription("This is a test post");
        postDto.setPicture("test.jpg");
        postDto.setRating(4);

        userPostDto = new UserPostDto();
        userPostDto.setPlaceId("place1");
        userPostDto.setTitle("Test Post");
        userPostDto.setDescription("This is a test post");
        userPostDto.setPicture("test.jpg");
        userPostDto.setRating(4);

        user = new User();
        user.setId(1);
        user.setPoint(10);

        place = new Place();
        place.setId("place1");
        place.setRating(3);

        post = Post.builder()
                .title(postDto.getTitle())
                .description(postDto.getDescription())
                .picture(postDto.getPicture())
                .rating(postDto.getRating())
                .user(user)
                .place(place)
                .build();
    }

    @Test
    void create_WhenUserAndPlaceNotNull_ReturnPost() {
        // arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // act
        Post result = postServiceImpl.create(postDto);

        // assert
        assertNotNull(result);
        assertEquals(result, post);
    }

    @Test
    void create_WhenUserNull_ReturnRuntimeException() {
        // arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // act and assert
        Exception exception = assertThrows(RuntimeException.class, () -> postServiceImpl.create(postDto));
        assertEquals("User not found", exception.getMessage());
        verifyNoInteractions(placeRepository, postRepository, geoApiService);
    }

    @Test
    void create_WhenPlaceNull_ReturnPost() {
        // arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(placeRepository.findById("place1")).thenReturn(Optional.empty());
        when(geoApiService.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // act
        Post result = postServiceImpl.create(postDto);

        //assert
        assertEquals(result, post);
        assertNotNull(result);
    }

    // cloudinary test

    @Test
    void findAll_WhenSuccess() {
        // arrange
        when(postRepository.findAll()).thenReturn(List.of(Post.builder().id(1).build(), Post.builder().id(2).build()));

        // act
        List<Post> results = postServiceImpl.findAll();

        // assert
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    void findById_WhenSuccess() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        Post result = postServiceImpl.findById(1);

        assertNotNull(result);
        assertEquals(post, result);
    }

    @Test
    void findById_WhenNotFound() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postServiceImpl.findById(1);
        });
        assertEquals(exception.getMessage(), "Post not found");
    }

    @Test
    void update_WhenUserNotFound_ReturnRuntimeException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> postServiceImpl.update(1, postDto));
        assertEquals("User not found", exception.getMessage());
        verifyNoInteractions(placeRepository, postRepository, geoApiService);
    }

    @Test
    void update_WhenPlaceNotFound_ReturnRuntimeException() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(placeRepository.findById("place1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> postServiceImpl.update(1, postDto));
        assertEquals("Place not found", exception.getMessage());
        verifyNoInteractions(postRepository);
    }

    @Test
    void update_WhenPostNotFound_ReturnRuntimeException() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> postServiceImpl.update(1, postDto));
        assertEquals(exception.getMessage(), "Post not found");

    }

    @Test
    void update_WhenSuccess_ReturnPost() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postServiceImpl.update(1, postDto);

        assertNotNull(result);
        assertEquals(result, post);

        verify(placeRepository, times(1)).save(place);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void deleteById_WhenIdNotNull_DeletedPost() {
        postServiceImpl.deleteById(1);

        verify(postRepository).deleteById(1);
    }

    @Test
    void deleteById_WhenIdNull_DoNothing() {
        postServiceImpl.deleteById(null);

        verify(postRepository, never()).deleteById(1);
    }

    @Test
    void findByUser_WhenUserNotFound_ReturnRuntimeException() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> postServiceImpl.findByUser(1));
        assertEquals(exception.getMessage(), "Post not found");
    }

    @Test
    void findByUser_WhenSuccess_ReturnPost() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        Post result = postServiceImpl.findByUser(1);

        assertEquals(result, post);
        assertNotNull(result);
    }

    @Test
    void findAllByUser_WhenSuccess_ReturnListOfPost() {
        user.setPosts(List.of(Post.builder().id(1).build(), Post.builder().id(2).build()));
        when(authenticationService.getUserAuthenticated()).thenReturn(user);

        List<Post> posts = postServiceImpl.findAllByUser();

        assertNotNull(posts);
        assertEquals(2, posts.size());
    }


    @Test
    void createByUser_WhenPlaceFromGeoApi_ReturnPost() {
        when(authenticationService.getUserAuthenticated()).thenReturn(user);
        when(placeRepository.findById("place1")).thenReturn(Optional.empty());
        when(geoApiService.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postServiceImpl.createByUser(userPostDto);

        assertNotNull(result);
        assertEquals(post, result);
    }


    @Test
    void createByUser_WhenPlaceFromRepo_ReturnPost() {
        when(authenticationService.getUserAuthenticated()).thenReturn(user);
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postServiceImpl.createByUser(userPostDto);

        assertNotNull(result);
        assertEquals(post, result);

        verifyNoInteractions(geoApiService);

    }

    @Test
    void createByUser_WhenPlaceFromRepoAndGeoApiIsNull_ReturnException() {
        when(authenticationService.getUserAuthenticated()).thenReturn(user);
        when(placeRepository.findById("place1")).thenReturn(Optional.empty());
        when(geoApiService.findById("place1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> postServiceImpl.createByUser(userPostDto));
        assertEquals(exception.getMessage(), "Place not found");

    }

    @Test
    void createByUser_WhenSuccess_ReturnPost() {
        when(authenticationService.getUserAuthenticated()).thenReturn(user);
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postServiceImpl.createByUser(userPostDto);

        assertNotNull(result);
        assertEquals(post, result);
    }

    @Test
    void updateByUser_WhenPlaceFromRepo_ReturnPost() {
        when(authenticationService.getUserAuthenticated()).thenReturn(user);
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postServiceImpl.updateByUser(1, userPostDto);

        assertNotNull(result);
        assertEquals(post, result);

        verify(placeRepository).save(place);
    }

    @Test
    void updateByUser_WhenPlaceFromGeoApi_ReturnPost() {
        when(authenticationService.getUserAuthenticated()).thenReturn(user);
        when(placeRepository.findById("place1")).thenReturn(Optional.empty());
        when(geoApiService.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postServiceImpl.updateByUser(1, userPostDto);

        assertNotNull(result);
        assertEquals(post, result);

        verify(placeRepository).save(place);
        verify(geoApiService, times(1)).findById("place1");
    }

    @Test
    void updateByUser_WhenPlaceNotFound_ReturnException() {
        when(authenticationService.getUserAuthenticated()).thenReturn(user);
        when(placeRepository.findById("place1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> postServiceImpl.updateByUser(1, userPostDto));
        assertEquals("Place not found", exception.getMessage());
        verifyNoInteractions(postRepository);
    }

    @Test
    void updateByUser_WhenPostNotFound_ReturnException() {
        when(authenticationService.getUserAuthenticated()).thenReturn(user);
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> postServiceImpl.updateByUser(1, userPostDto));
        assertEquals(exception.getMessage(), "Post not found");
    }

    @Test
    void updateByUser_WhenSuccess_ReturnPost() {
        when(authenticationService.getUserAuthenticated()).thenReturn(user);
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postServiceImpl.updateByUser(1, userPostDto);

        assertNotNull(result);
        assertEquals(result, post);

        verify(placeRepository, times(1)).save(place);
        verify(postRepository, times(1)).save(post);
    }


    @Test
    void deleteByUser_WhenIdNotNull_DeletedPost() {
        postServiceImpl.deleteByUser(1);

        verify(postRepository).deleteById(1);
    }

    @Test
    void deleteByUser_WhenIdNull_DoNothing() {
        postServiceImpl.deleteByUser(null);

        verify(postRepository, never()).deleteById(1);
    }


}
