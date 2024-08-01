package com.virgo.rekomendasos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virgo.rekomendasos.model.meta.Post;
import com.virgo.rekomendasos.service.impl.PostServiceImpl;
import com.virgo.rekomendasos.utils.dto.PostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PostControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PostServiceImpl postServiceImpl;

    @InjectMocks
    private PostController postController;

    private Post post1;
    private Post post2;
    private PostDto postDto;
    private List<Post> posts;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(postController)
                .build();

        post1 = Post.builder().id(1)
                .title("Main Yok")
                .description("deskripsi")
                .rating(2)
                .picture("kosong")
                .build();
        post2 = Post.builder().id(2)
                .title("Main Ada")
                .description("deskripsi")
                .rating(2)
                .picture("kosong")
                .build();

        postDto = PostDto.builder()
                .title("Main Yok")
                .description("deskripsi")
                .rating(2)
                .picture("kosong")
                .build();

        posts = new ArrayList<>(Arrays.asList(post1, post2));
    }

    @Test
    void findAll_WhenSuccess_ReturnAllPosts() throws Exception {
        when(postServiceImpl.findAll()).thenReturn(posts);
        mockMvc.perform(get("/api/admin/posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Main Yok"))
                .andExpect(jsonPath("$.data[0].description").value("deskripsi"))
                .andExpect(jsonPath("$.data[0].rating").value(2))
                .andExpect(jsonPath("$.data[0].picture").value("kosong"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].title").value("Main Ada"))
                .andExpect(jsonPath("$.data[1].description").value("deskripsi"))
                .andExpect(jsonPath("$.data[1].rating").value(2))
                .andExpect(jsonPath("$.data[1].picture").value("kosong"));

    }

    @Test
    void findById_WhenSuccess_ReturnPost() throws Exception {
        when(postServiceImpl.findById(1)).thenReturn(post1);
        mockMvc.perform(get("/api/admin/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Main Yok"))
                .andExpect(jsonPath("$.data.description").value("deskripsi"))
                .andExpect(jsonPath("$.data.rating").value(2))
                .andExpect(jsonPath("$.data.picture").value("kosong"));
    }

    @Test
    void create_WhenSuccess_ReturnPost() throws Exception {

    }

    @Test
    void update_WhenSuccess_ReturnPost() throws Exception {
        when(postServiceImpl.update(1, postDto)).thenReturn(post1);

        mockMvc.perform(put("/api/admin/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void delete_WhenSuccess_ReturnPost() throws Exception {

        mockMvc.perform(delete("/api/admin/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findAllUserPosts_WhenSuccess_ReturnPosts() throws Exception {
        when(postServiceImpl.findAllByUser()).thenReturn(posts);
        mockMvc.perform(get("/api/user/posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Main Yok"))
                .andExpect(jsonPath("$.data[0].description").value("deskripsi"))
                .andExpect(jsonPath("$.data[0].rating").value(2))
                .andExpect(jsonPath("$.data[0].picture").value("kosong"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].title").value("Main Ada"))
                .andExpect(jsonPath("$.data[1].description").value("deskripsi"))
                .andExpect(jsonPath("$.data[1].rating").value(2))
                .andExpect(jsonPath("$.data[1].picture").value("kosong"));
    }

    @Test
    void findUserPost_WhenSuccess_ReturnPost() throws Exception {
        when(postServiceImpl.findByUser(1)).thenReturn(post1);
        mockMvc.perform(get("/api/user/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Main Yok"))
                .andExpect(jsonPath("$.data.description").value("deskripsi"))
                .andExpect(jsonPath("$.data.rating").value(2))
                .andExpect(jsonPath("$.data.picture").value("kosong"));
    }

    @Test
    void createUserPost_WhenSuccess_ReturnPost() throws Exception {

    }

    @Test
    void updateUserPost_WhenSuccess_ReturnPost() throws Exception {
        when(postServiceImpl.update(1, postDto)).thenReturn(post1);
        mockMvc.perform(put("/api/user/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteUserPost_WhenSuccess_ReturnPost() throws Exception {

        mockMvc.perform(delete("/api/user/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findAllPublicPosts_WhenSuccess_ReturnPosts() throws Exception {
        when(postServiceImpl.findAll()).thenReturn(posts);
        mockMvc.perform(get("/api/posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Main Yok"))
                .andExpect(jsonPath("$.data[0].description").value("deskripsi"))
                .andExpect(jsonPath("$.data[0].rating").value(2))
                .andExpect(jsonPath("$.data[0].picture").value("kosong"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].title").value("Main Ada"))
                .andExpect(jsonPath("$.data[1].description").value("deskripsi"))
                .andExpect(jsonPath("$.data[1].rating").value(2))
                .andExpect(jsonPath("$.data[1].picture").value("kosong"));
    }
}
