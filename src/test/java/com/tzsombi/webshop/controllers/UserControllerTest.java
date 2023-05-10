package com.tzsombi.webshop.controllers;

import com.tzsombi.webshop.AbstractTestContainer;
import com.tzsombi.webshop.auth.JwtService;
import com.tzsombi.webshop.models.Role;
import com.tzsombi.webshop.models.User;
import com.tzsombi.webshop.repositories.UserRepository;
import com.tzsombi.webshop.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest extends AbstractTestContainer {

    public static final String baseEndpointUrl = "/api/v1/users";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void itShould_getUserById() throws Exception {
        // given
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "someemail@gmail.com";
        String password = "password";
        Role role = Role.USER;
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(role)
                .build();
        User savedUser = userRepository.save(user);

        String expected = "{\"firstName\":\"firstName\"," +
                "\"lastName\":\"lastName\"," +
                "\"email\":\"someemail@gmail.com\"," +
                "\"sellingProducts\":[],\"role\":\"USER\"}";
        // when
        String token = jwtService.generateToken(user);
        // then
        MvcResult result = mockMvc.perform(get(baseEndpointUrl + "/{userId}", savedUser.getId())
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(expected);
    }

    @Test
    void itShould_updateUser() throws Exception {
        // given
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "someemail@gmail.com";
        String password = "password";
        Role role = Role.USER;
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(role)
                .build();
        User savedUser = userRepository.save(user);

        String expectedFirstName = "anotherFirstName";
        String expectedLastName = "anotherLastName";
        String expectedEmail = "someemail2@gmail.com";
        String expectedPassword = "anotherPw";
        String inputDto = "{\"firstName\":\"anotherFirstName\"," +
                "\"lastName\":\"anotherLastName\"," +
                "\"email\":\"someemail2@gmail.com\"," +
                "\"password\":\"anotherPw\"}";
        String token = jwtService.generateToken(user);

        // when
        MvcResult result = mockMvc.perform(put(baseEndpointUrl + "/update/{userId}", savedUser.getId())
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputDto))
                .andExpect(status().isOk()).andReturn();

        User userAfter = userRepository.findById(savedUser.getId())
                .orElse(null);
        String expectedToken = jwtService.generateToken(userAfter);
        // then
        assert userAfter != null;
        //assertThat(result.getResponse().getContentAsString()).isEqualTo(expectedToken);
        assertThat(userAfter.getFirstName()).isEqualTo(expectedFirstName);
        assertThat(userAfter.getLastName()).isEqualTo(expectedLastName);
        assertThat(userAfter.getEmail()).isEqualTo(expectedEmail);
        assertThat(passwordEncoder.matches(expectedPassword, userAfter.getPassword())).isTrue();
    }

    @Test
    void itShould_deleteUser() throws Exception {
        // given
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "someemail@gmail.com";
        String password = "password";
        Role role = Role.USER;
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(role)
                .build();
        User savedUser = userRepository.save(user);

        // when
        String token = jwtService.generateToken(user);

        mockMvc.perform(delete(baseEndpointUrl + "/delete/{userId}", savedUser.getId())
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();

        User userAfter = userRepository.findById(savedUser.getId())
                .orElse(null);
        // then
        assertThat(userAfter).isNull();
    }
}