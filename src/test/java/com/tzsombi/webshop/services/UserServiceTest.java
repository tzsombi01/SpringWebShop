package com.tzsombi.webshop.services;

import com.tzsombi.webshop.auth.AuthenticationResponse;
import com.tzsombi.webshop.auth.JwtService;
import com.tzsombi.webshop.constants.ErrorConstants;
import com.tzsombi.webshop.exceptions.AuthException;
import com.tzsombi.webshop.exceptions.UserNotFoundException;
import com.tzsombi.webshop.models.Role;
import com.tzsombi.webshop.models.User;
import com.tzsombi.webshop.models.UserRequestDTO;
import com.tzsombi.webshop.models.UserResponseDTO;
import com.tzsombi.webshop.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {

    private UserService underTestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserResponseDTOMapper userResponseDTOMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        underTestService = new UserService(userRepository, userResponseDTOMapper, passwordEncoder, jwtService);
    }

    @Test
    void itShould_GetUserById_WhenUserExists() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User expected = userRepository.save(user);
        // when
        UserResponseDTO foundUserDto  = underTestService.getUserById(expected.getId());

        // then
        assertThat(foundUserDto).isInstanceOf(UserResponseDTO.class);
        assertThat(foundUserDto.firstName()).isEqualTo(expected.getFirstName());
        assertThat(foundUserDto.lastName()).isEqualTo(expected.getLastName());
        assertThat(foundUserDto.email()).isEqualTo(expected.getEmail());
        assertThat(foundUserDto.role()).isEqualTo(expected.getRole());
    }

    @Test
    void itShould_GetUserById_WhenMultipleUsersExists() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User user2 = User.builder()
                .firstName("firstName2")
                .lastName("lastName2")
                .email("someemail2@gmail.com")
                .password("password2")
                .role(Role.USER)
                .build();
        User user3 = User.builder()
                .firstName("firstName3")
                .lastName("lastName3")
                .email("someemail3@gmail.com")
                .password("password3")
                .role(Role.USER)
                .build();
        User expected = userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        // when
        UserResponseDTO foundUserDto  = underTestService.getUserById(expected.getId());

        // then
        assertThat(foundUserDto).isInstanceOf(UserResponseDTO.class);
        assertThat(foundUserDto.firstName()).isEqualTo(expected.getFirstName());
        assertThat(foundUserDto.lastName()).isEqualTo(expected.getLastName());
        assertThat(foundUserDto.email()).isEqualTo(expected.getEmail());
        assertThat(foundUserDto.role()).isEqualTo(expected.getRole());
    }

    @Test
    void itShouldNot_GetUserById_WhenUserDoesNotExists_ThrowsUserNotFoundException() {
        // given
        // when
        // then
        assertThatThrownBy(() -> underTestService.getUserById(1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(ErrorConstants.USER_NOT_FOUND_MSG);
    }

    @Test
    void itShouldNot_GetUserById_WhenMultipleOtherUsersExist_ButIdNotAssigned_ThrowsUserNotFoundException() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User user2 = User.builder()
                .firstName("firstName2")
                .lastName("lastName2")
                .email("someemail2@gmail.com")
                .password("password2")
                .role(Role.USER)
                .build();
        User user3 = User.builder()
                .firstName("firstName3")
                .lastName("lastName3")
                .email("someemail3@gmail.com")
                .password("password3")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        // when
        // then
        assertThatThrownBy(() -> underTestService.getUserById(4L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(ErrorConstants.USER_NOT_FOUND_MSG);
    }

    @Test
    void itShould_updateUserById_ExistingUserValidInput() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        String expectedFirstName = "expectedFirstName";
        String expectedLastName = "expectedLastName";
        String expectedEmail = "expectedemail@gmail.com";
        String expectedPassword = "expectedPassword";
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                expectedFirstName,
                expectedLastName,
                expectedEmail,
                expectedPassword
        );
        // when
        AuthenticationResponse response = underTestService.updateUserById(savedUser.getId(), userRequestDTO);
        User updatedUser = userRepository.findById(savedUser.getId())
                .orElse(null);
        // then
        String token = response.getToken();
        assertThat(jwtService.extractUsername(token)).isEqualTo(expectedEmail);
        assertThat(updatedUser.getFirstName()).isEqualTo(expectedFirstName);
        assertThat(updatedUser.getLastName()).isEqualTo(expectedLastName);
        assertThat(updatedUser.getEmail()).isEqualTo(expectedEmail);
        assertThat(passwordEncoder.matches(expectedPassword, updatedUser.getPassword())).isTrue();
    }

    @Test
    void itShouldNot_updateUserById_ExistingUserNoInputs() {
        // given
        String expectedFirstName = "expectedFirstName";
        String expectedLastName = "expectedLastName";
        String expectedEmail = "expectedemail@gmail.com";
        String expectedPassword = "expectedPassword";
        User user = User.builder()
                .firstName(expectedFirstName)
                .lastName(expectedLastName)
                .email(expectedEmail)
                .password(passwordEncoder.encode(expectedPassword))
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);

        UserRequestDTO userRequestDTO = new UserRequestDTO(
                null,
                null,
                null,
                null
        );

        // when
        AuthenticationResponse response = underTestService.updateUserById(savedUser.getId(), userRequestDTO);
        User updatedUser = userRepository.findById(savedUser.getId())
                .orElse(null);

        // then
        String token = response.getToken();
        assertThat(jwtService.extractUsername(token)).isEqualTo(expectedEmail);
        assertThat(updatedUser.getFirstName()).isEqualTo(expectedFirstName);
        assertThat(updatedUser.getLastName()).isEqualTo(expectedLastName);
        assertThat(updatedUser.getEmail()).isEqualTo(expectedEmail);
        assertThat(passwordEncoder.matches(expectedPassword, updatedUser.getPassword())).isTrue();
    }

    @Test
    void itShouldThrow_updateUserById_NotExistingUser() {
        // given
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                null,
                null,
                null,
                null
        );
        // when
        // then
        assertThatThrownBy(() -> underTestService.updateUserById(1L, userRequestDTO))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(ErrorConstants.USER_NOT_FOUND_MSG);
    }

    @Test
    void itShouldThrow_updateUserById_ExistingUserBadEmailFormat() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                null,
                null,
                "bademail.com",
                null
        );
        // when
        // then
        assertThatThrownBy(() -> underTestService.updateUserById(savedUser.getId(), userRequestDTO))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(AuthException.class)
                .hasMessageContaining(ErrorConstants.INVALID_EMAIL_FORMAT_MSG);
    }

    @Test
    void itShould_DeleteUserById_ExistingUser() {
        // given
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("someemail@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        // when
        underTestService.deleteUserById(savedUser.getId());
        // then
        assertThat(userRepository.existsById(savedUser.getId())).isFalse();
    }

    @Test
    void itShouldThrow_DeleteUserById_NotExistingUser() {
        // given
        // when
        // then
        assertThatThrownBy(() -> underTestService.deleteUserById(1L))
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(ErrorConstants.USER_NOT_FOUND_MSG);
    }
}