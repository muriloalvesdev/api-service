package br.com.api.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import br.com.api.constants.UserConstantsForTests;
import br.com.api.domain.model.Role;
import br.com.api.domain.model.User;
import br.com.api.domain.model.Role.RoleName;
import br.com.api.domain.repository.UserRepository;
import br.com.api.exception.EmailNotFoundException;
import br.com.api.provider.UserDetailsProviderTest;

class UserDetailsServiceImplTest implements UserConstantsForTests {

  private UserDetailsService service;
  private UserRepository userRepository;
  private HashSet<Role> roles;

  @BeforeEach
  void setUp() {
    this.roles = new HashSet<>();
    this.roles.add(new Role(RoleName.ROLE_ADMIN));
    this.userRepository = mock(UserRepository.class);
    this.service = new UserDetailsServiceImpl(this.userRepository);
  }

  @ParameterizedTest
  @ArgumentsSource(UserDetailsProviderTest.class)
  void loadUserByUserNameWithSuccess(UserDetails userDetails) {
    // given
    User user = new User(UUID.randomUUID(), FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);
    user.setRoles(this.roles);
    // when
    BDDMockito.when(this.userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

    // then
    UserDetails userDetailsActual = this.service.loadUserByUsername(EMAIL);

    verify(this.userRepository, times(1)).findByEmail(Mockito.anyString());
    assertEquals(userDetails.getUsername(), userDetailsActual.getUsername());
  }

  @Test
  void loadUserByUserNameWithError() {
    // given
    BDDMockito.given(this.userRepository.findByEmail(EMAIL)).willReturn(Optional.ofNullable(null));

    // then
    Exception exception = assertThrows(Exception.class, () -> {
      this.service.loadUserByUsername(EMAIL);
    });

    assertTrue(exception instanceof EmailNotFoundException);
    assertEquals(String.format(UserDetailsServiceImpl.EMAIL_NOT_FOUND, EMAIL),
        exception.getMessage());

    verify(this.userRepository, times(1)).findByEmail(Mockito.anyString());
  }
}
