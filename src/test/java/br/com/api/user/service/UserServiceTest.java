package br.com.api.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.com.api.config.jwt.JwtProvider;
import br.com.api.constants.UserConstantsForTests;
import br.com.api.domain.model.Role;
import br.com.api.domain.model.User;
import br.com.api.domain.model.Role.RoleName;
import br.com.api.domain.repository.RoleRepository;
import br.com.api.domain.repository.UserRepository;
import br.com.api.dto.RegisterDTO;
import br.com.api.dto.UserDTO;
import br.com.api.exception.EmailNotFoundException;
import br.com.api.exception.ExistingEmailException;
import br.com.api.exception.IllegalRoleException;
import br.com.api.provider.RegisterDTOProviderTests;

class UserServiceTest implements UserConstantsForTests {

  private UserService service;
  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder encoder;
  private JwtProvider jwtProvider;
  private AuthenticationManager authenticationManager;
  private Role role;
  private HashSet<Role> roles;
  private User user;

  @BeforeEach
  void setUp() {
    this.role = new Role(RoleName.ROLE_ADMIN);
    this.role.setId(1L);
    this.roles = new HashSet<>();
    this.roles.add(this.role);
    this.user = new User();
    this.user.setId(UUID.randomUUID());
    this.user.setEmail(EMAIL);
    this.user.setName(FIRST_NAME);
    this.user.setLastName(LAST_NAME);
    this.user.setPassword(PASSWORD_ENCRYPT);
    this.user.setRoles(this.roles);
    this.userRepository = mock(UserRepository.class);
    this.roleRepository = mock(RoleRepository.class);
    this.encoder = mock(PasswordEncoder.class);
    this.jwtProvider = mock(JwtProvider.class);
    this.authenticationManager = mock(AuthenticationManager.class);

    this.service = new UserService(this.userRepository, this.roleRepository, this.encoder,
        this.jwtProvider, this.authenticationManager);
  }

  @ParameterizedTest
  @ArgumentsSource(RegisterDTOProviderTests.class)
  void registerUserWithError(RegisterDTO registerDTO) {
    // given
    BDDMockito.given(this.userRepository.existsByEmail(Mockito.anyString()))
        .willReturn(Boolean.TRUE);

    // then
    Exception exception = assertThrows(Exception.class, () -> {
      this.service.register(registerDTO);
    });

    assertTrue(exception instanceof ExistingEmailException);
    assertEquals(UserService.EMAIL_IS_ALREADY, exception.getMessage());
  }

  @Test
  void shouldFindByEmailWithSuccess() {
    // when
    BDDMockito.when(this.userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(this.user));

    // then
    UserDTO userDTO = this.service.find(EMAIL);

    verify(this.userRepository, times(1)).findByEmail(Mockito.anyString());

    assertEquals(this.user.getName(), userDTO.getName());
    assertEquals(this.user.getLastName(), userDTO.getLastName());
    assertEquals(this.user.getEmail(), userDTO.getEmail());
  }

  @Test
  void shouldDelete() {
    // when
    BDDMockito.when(this.userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(this.user));
    BDDMockito.doNothing().when(this.userRepository).delete(this.user);

    // then
    this.service.delete(EMAIL);

    verify(this.userRepository, times(1)).findByEmail(Mockito.anyString());
    verify(this.userRepository, times(1)).delete(Mockito.any());
  }

  @ParameterizedTest
  @ArgumentsSource(RegisterDTOProviderTests.class)
  void updateWithErrorByRoles(RegisterDTO registerDTO) {
    // when
    BDDMockito.when(this.roleRepository.findByName(RoleName.ROLE_ADMIN))
        .thenReturn(Optional.empty());
    BDDMockito.when(this.userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(this.user));
    BDDMockito.when(this.encoder.encode(registerDTO.getPassword())).thenReturn(PASSWORD_ENCRYPT);

    // then
    Exception exception = assertThrows(Exception.class, () -> {
      this.service.update(registerDTO);
    });

    assertTrue(exception instanceof IllegalRoleException);
    assertEquals(String.format(UserService.ROLE_NOT_FOUND, "Admin"), exception.getMessage());
  }

  @ParameterizedTest
  @ArgumentsSource(RegisterDTOProviderTests.class)
  void updateWithErrorByEmail(RegisterDTO registerDTO) {
    // when
    BDDMockito.when(this.userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

    // then
    Exception exception = assertThrows(Exception.class, () -> {
      this.service.find(EMAIL);
    });

    verify(this.userRepository, times(1)).findByEmail(Mockito.anyString());

    assertTrue(exception instanceof EmailNotFoundException);
    assertEquals(UserService.EMAIL_NOT_FOUND, exception.getMessage());
  }

  @ParameterizedTest
  @ArgumentsSource(RegisterDTOProviderTests.class)
  void updateWithSuccess(RegisterDTO registerDTO) {
    // when
    BDDMockito.when(this.userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(this.user));
    BDDMockito.when(this.userRepository.saveAndFlush(this.user)).thenReturn(this.user);
    BDDMockito.when(this.encoder.encode(registerDTO.getPassword())).thenReturn(PASSWORD_ENCRYPT);
    BDDMockito.when(this.roleRepository.findByName(RoleName.ROLE_ADMIN))
        .thenReturn(Optional.of(this.role));

    // then
    UserDTO userDTO = this.service.update(registerDTO);

    verify(this.userRepository, times(1)).findByEmail(Mockito.anyString());
    verify(this.userRepository, times(1)).saveAndFlush(Mockito.any());
    verify(this.encoder, times(1)).encode(Mockito.any());
    verify(this.roleRepository, times(1)).findByName(Mockito.any());

    assertEquals(EMAIL, userDTO.getEmail());
    assertEquals(FIRST_NAME.toUpperCase(), userDTO.getName());
    assertEquals(LAST_NAME.toUpperCase(), userDTO.getLastName());
    assertEquals(PASSWORD_ENCRYPT, userDTO.getPassword());
  }

  @Test
  void shouldFindAll() {
    // given
    List<User> users = new ArrayList<>();
    users.add(this.user);

    // when
    BDDMockito.when(this.userRepository.findAll()).thenReturn(users);

    // then
    List<UserDTO> usersDTO = this.service.find();

    verify(this.userRepository, times(1)).findAll();

    assertEquals(users.size(), usersDTO.size());
  }

  @Test
  void shouldFindByEmailWithError() {
    // when
    BDDMockito.when(this.userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

    // then
    Exception exception = assertThrows(Exception.class, () -> {
      this.service.find(EMAIL);
    });

    assertTrue(exception instanceof EmailNotFoundException);
    assertEquals(UserService.EMAIL_NOT_FOUND, exception.getMessage());

    verify(this.userRepository, times(1)).findByEmail(Mockito.anyString());
  }

  @ParameterizedTest
  @ArgumentsSource(RegisterDTOProviderTests.class)
  void registerUserWithSuccess(RegisterDTO registerDTO) {
    // when
    BDDMockito.when(this.userRepository.existsByEmail(Mockito.anyString()))
        .thenReturn(Boolean.FALSE);
    BDDMockito.when(this.roleRepository.findByName(Mockito.any())).thenReturn(Optional.of(role));
    BDDMockito.when(this.userRepository.saveAndFlush(Mockito.any())).thenReturn(user);
    BDDMockito.when(this.encoder.encode(Mockito.anyString())).thenReturn(PASSWORD_ENCRYPT);

    // then
    User userActual = this.service.register(registerDTO);

    verify(this.userRepository, times(1)).existsByEmail(Mockito.anyString());
    verify(this.roleRepository, times(1)).findByName(Mockito.any());
    verify(this.userRepository, times(1)).saveAndFlush(Mockito.any(User.class));

    assertEquals(EMAIL, userActual.getEmail());
    assertEquals(FIRST_NAME, userActual.getName());
    assertEquals(LAST_NAME, userActual.getLastName());
    assertEquals(PASSWORD_ENCRYPT, userActual.getPassword());
  }
}
