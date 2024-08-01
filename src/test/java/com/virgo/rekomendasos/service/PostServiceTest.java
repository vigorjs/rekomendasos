package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.model.meta.Post;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.PlaceRepository;
import com.virgo.rekomendasos.repo.PostRepository;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.service.AuthenticationService;
import com.virgo.rekomendasos.service.CloudinaryService;
import com.virgo.rekomendasos.service.PlaceService;
import com.virgo.rekomendasos.service.impl.PostServiceImpl;
import com.virgo.rekomendasos.utils.dto.PostDto;
import com.virgo.rekomendasos.utils.dto.UserPostDto;
import com.virgo.rekomendasos.utils.dto.restClientDto.CloudinaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSuccess() {
        PostDto postDto = new PostDto("Test Title", "Test Description", null, 4, 1, "place1");
        User user = new User();
        user.setId(1);
        user.setPoint(0L);
        Place place = new Place();
        place.setId("place1");
        place.setRating(0);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.save(any(Post.class))).thenAnswer(i -> i.getArguments()[0]);

        Post result = postService.create(postDto);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(4, result.getRating());
        assertEquals(user, result.getUser());
        assertEquals(place, result.getPlace());
        assertEquals(2L, user.getPoint());
        assertEquals(4, place.getRating());

        verify(userRepository).save(user);
        verify(placeRepository).save(place);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void testCreateWithFileSuccess() {
        PostDto postDto = new PostDto("Test Title", "Test Description", null, 4, 1, "place1");
        User user = new User();
        user.setId(1);
        user.setPoint(0L);
        Place place = new Place();
        place.setId("place1");
        place.setRating(0);
        MultipartFile file = mock(MultipartFile.class);
        CloudinaryResponse cloudinaryResponse = new CloudinaryResponse("public_id", "http://example.com/image.jpg");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(cloudinaryService.uploadFile(any(), anyString())).thenReturn(cloudinaryResponse);
        when(postRepository.save(any(Post.class))).thenAnswer(i -> i.getArguments()[0]);

        Post result = postService.create(postDto, file);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(4, result.getRating());
        assertEquals(user, result.getUser());
        assertEquals(place, result.getPlace());
        assertEquals("http://example.com/image.jpg", result.getPicture());
        assertEquals("public_id", result.getCloudinaryImageId());
        assertEquals(2L, user.getPoint());
        assertEquals(4, place.getRating());

        verify(userRepository).save(user);
        verify(placeRepository).save(place);
        verify(cloudinaryService).uploadFile(file, anyString());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void testCreateUserNotFound() {
        PostDto postDto = new PostDto("Test Title", "Test Description", null, 4, 1, "place1");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> postService.create(postDto));
    }

    @Test
    void testCreatePlaceNotFound() {
        PostDto postDto = new PostDto("Test Title", "Test Description", null, 4, 1, "place1");
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(placeRepository.findById("place1")).thenReturn(Optional.empty());
        when(placeService.getAllPlacesFromApi()).thenReturn(new ArrayList<>());

        assertThrows(RuntimeException.class, () -> postService.create(postDto));
    }

    @Test
    void testFindAllSuccess() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());

        when(postRepository.findAll()).thenReturn(posts);

        List<Post> result = postService.findAll();

        assertEquals(2, result.size());
        verify(postRepository).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        Post post = new Post();
        post.setId(1);

        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        Post result = postService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(postRepository).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> postService.findById(1));
    }

    @Test
    void testUpdateSuccess() {
        PostDto postDto = new PostDto("Updated Title", "Updated Description", null, 5, 1, "place1");
        User user = new User();
        user.setId(1);
        Place place = new Place();
        place.setId("place1");
        place.setRating(4);
        Post existingPost = new Post();
        existingPost.setId(1);
        existingPost.setRating(4);
        existingPost.setPlace(place);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.findById(1)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenAnswer(i -> i.getArguments()[0]);

        Post result = postService.update(1, postDto);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(5, result.getRating());
        assertEquals(user, result.getUser());
        assertEquals(place, result.getPlace());
        assertEquals(5, place.getRating());

        verify(placeRepository).save(place);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void testUpdatePostNotFound() {
        PostDto postDto = new PostDto("Updated Title", "Updated Description", null, 5, 1, "place1");
        User user = new User();
        user.setId(1);
        Place place = new Place();
        place.setId("place1");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> postService.update(1, postDto));
    }

    @Test
    void testDeleteByIdSuccess() {
        doNothing().when(postRepository).deleteById(1);

        postService.deleteById(1);

        verify(postRepository).deleteById(1);
    }

    @Test
    void testFindAllByUserSuccess() {
        User user = new User();
        user.setId(1);
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());
        user.setPosts(posts);

        when(authenticationService.getUserAuthenticated()).thenReturn(user);

        List<Post> result = postService.findAllByUser();

        assertEquals(2, result.size());
        verify(authenticationService).getUserAuthenticated();
    }

    @Test
    void testCreateByUserSuccess() {
        UserPostDto userPostDto = new UserPostDto("Test Title", "Test Description", null, 4, "place1");
        User user = new User();
        user.setId(1);
        user.setPoint(0L);
        Place place = new Place();
        place.setId("place1");
        place.setRating(0);

        when(authenticationService.getUserAuthenticated()).thenReturn(user);
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.save(any(Post.class))).thenAnswer(i -> i.getArguments()[0]);

        Post result = postService.createByUser(userPostDto);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(4, result.getRating());
        assertEquals(user, result.getUser());
        assertEquals(place, result.getPlace());
        assertEquals(2L, user.getPoint());
        assertEquals(4, place.getRating());

        verify(userRepository).save(user);
        verify(placeRepository).save(place);
    }

    @Test
    void testUpdateByUserSuccess() {
        UserPostDto userPostDto = new UserPostDto("Updated Title", "Updated Description", null, 5, "place1");
        User user = new User();
        user.setId(1);
        Place place = new Place();
        place.setId("place1");
        place.setRating(4);
        Post existingPost = new Post();
        existingPost.setId(1);
        existingPost.setRating(4);
        existingPost.setPlace(place);

        when(authenticationService.getUserAuthenticated()).thenReturn(user);
        when(placeRepository.findById("place1")).thenReturn(Optional.of(place));
        when(postRepository.findById(1)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenAnswer(i -> i.getArguments()[0]);

        Post result = postService.updateByUser(1, userPostDto);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(5, result.getRating());
        assertEquals(user, result.getUser());
        assertEquals(place, result.getPlace());
        assertEquals(5, place.getRating());

        verify(placeRepository).save(place);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void testDeleteByUserSuccess() {
        doNothing().when(postRepository).deleteById(1);

        postService.deleteByUser(1);

        verify(postRepository).deleteById(1);
    }
}
