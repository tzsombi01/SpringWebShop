package com.tzsombi.webshop.repositories;

import com.tzsombi.webshop.models.Role;
import com.tzsombi.webshop.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTestUserRepository;

    @AfterEach
    void tearDown() {
        underTestUserRepository.deleteAll();
    }

    @Test
    void itShould_findByEmail_ExistingEmail() {
        // given
        String email = "someemail@gmail.com";
        User expectedUser = User
                .builder()
                .firstName("firstName")
                .lastName("lastName")
                .email(email)
                .password("password")
                .role(Role.USER)
                .build();
        underTestUserRepository.save(expectedUser);

        // when
        User foundUser = underTestUserRepository.findByEmail(email)
                .orElse(null);

        // then
        assertThat(expectedUser).isEqualTo(foundUser);
    }

    @Test
    void itShouldNot_findByEmail_NotExistingEmail() {
        // given
        String email = "someemail@gmail.com";
        String anotherEmail = "anotheremail@gmail.com";
        User user = User
                .builder()
                .firstName("firstName")
                .lastName("lastName")
                .email(email)
                .password("password")
                .role(Role.USER)
                .build();
        underTestUserRepository.save(user);

        // when
        User foundUser = underTestUserRepository.findByEmail(anotherEmail)
                .orElse(null);

        // then
        assertThat(foundUser).isEqualTo(null);
    }

    @Test
    void itShouldBe_True_existsByEmail() {
        // given
        String email = "someemail@gmail.com";
        User user = User
                .builder()
                .firstName("firstName")
                .lastName("lastName")
                .email(email)
                .password("password")
                .role(Role.USER)
                .build();
        underTestUserRepository.save(user);
        // when
        boolean exists = underTestUserRepository.existsByEmail(email);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldBe_False_doesNotExistsByEmail() {
        // given
        String email = "someemail@gmail.com";
        String anotherEmail = "anotheremail@gmail.com";
        User user = User
                .builder()
                .firstName("firstName")
                .lastName("lastName")
                .email(email)
                .password("password")
                .role(Role.USER)
                .build();
        underTestUserRepository.save(user);
        // when
        boolean exists = underTestUserRepository.existsByEmail(anotherEmail);

        // then
        assertThat(exists).isFalse();
    }
}