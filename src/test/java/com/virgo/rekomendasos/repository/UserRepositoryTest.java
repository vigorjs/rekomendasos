package com.virgo.rekomendasos.repository;

import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenCreateUser_thenReturnCreatedUser() {
        User user = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user);

        User foundUser = userRepository.findById(user.getId()).orElse(null);

        assertThat(foundUser).isNotNull();
    }

    @Test
    public void whenGetAllUsers_thenReturnAllUsers() {
        User user1 = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        User user2 = User.builder()
                .firstname("Enigma 2")
                .lastname("Camp")
                .email("user2@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertThat(users).isNotNull();
        assertThat(users).hasSize(2);
    }

    @Test
    public void whenGetUserById_thenReturnUser() {
        User user = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user);

        User foundUser = userRepository.findById(user.getId()).orElse(null);

        assertThat(foundUser).isNotNull();
    }

    @Test
    public void whenUpdateUserById_thenReturnUpdatedUser() {
        User user = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user);

        User foundUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(foundUser).isNotNull();

        foundUser.setFirstname("Updated User");
        User updatedUser = userRepository.save(foundUser);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getFirstname()).isEqualTo("Updated User");
    }

    @Test
    public void whenDeleteUserById_thenUserShouldBeDeleted() {
        User user = User.builder()
                .firstname("Enigma")
                .lastname("Camp")
                .email("user@enigma.com")
                .password("enigma123")
                .build();
        userRepository.save(user);

        userRepository.deleteById(user.getId());
        User foundUser = userRepository.findById(user.getId()).orElse(null);

        assertThat(foundUser).isNull();
    }
}
