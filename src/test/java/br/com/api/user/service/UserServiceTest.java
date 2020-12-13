package br.com.api.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.com.api.config.jwt.JwtProvider;
import br.com.api.constants.UserConstantsForTests;
import br.com.api.exception.ExistingEmailException;
import br.com.api.provider.RegisterDTOProviderTests;
import br.com.api.user.dto.RegisterDTO;
import br.com.api.user.model.Role;
import br.com.api.user.model.Role.RoleName;
import br.com.api.user.model.User;
import br.com.api.user.repository.RoleRepository;
import br.com.api.user.repository.UserRepository;

class UserServiceTest implements UserConstantsForTests {

  private UserService service;
  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder encoder;
  private JwtProvider jwtProvider;
  private AuthenticationManager authenticationManager;
  private Role role;

  @BeforeEach
  void setUp() {
    role = new Role(RoleName.ROLE_ADMIN);
    role.setId(1L);
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
      this.service.registerUser(registerDTO);
    });

    assertTrue(exception instanceof ExistingEmailException);
    assertEquals(UserService.EMAIL_IS_ALREADY, exception.getMessage());
  }

  @ParameterizedTest
  @ArgumentsSource(RegisterDTOProviderTests.class)
  void registerUserWithSuccess(RegisterDTO registerDTO) {
    // given
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setEmail(EMAIL);
    user.setName(FIRST_NAME);
    user.setLastName(LAST_NAME);
    user.setPassword(PASSWORD_ENCRYPT);
    user.setRoles(Set.of(this.role));

    // when
    BDDMockito.when(this.userRepository.existsByEmail(Mockito.anyString()))
        .thenReturn(Boolean.FALSE);
    BDDMockito.when(this.roleRepository.findByName(Mockito.any())).thenReturn(Optional.of(role));
    BDDMockito.when(this.userRepository.saveAndFlush(Mockito.any())).thenReturn(user);
    BDDMockito.when(this.encoder.encode(Mockito.anyString())).thenReturn(PASSWORD_ENCRYPT);

    // then
    User userActual = this.service.registerUser(registerDTO);

    verify(this.userRepository, times(1)).existsByEmail(Mockito.anyString());
    verify(this.roleRepository, times(1)).findByName(Mockito.any());
    verify(this.userRepository, times(1)).saveAndFlush(Mockito.any(User.class));

    assertEquals(EMAIL, userActual.getEmail());
    assertEquals(FIRST_NAME, userActual.getName());
    assertEquals(LAST_NAME, userActual.getLastName());
    assertEquals(PASSWORD_ENCRYPT, userActual.getPassword());
  }
}
