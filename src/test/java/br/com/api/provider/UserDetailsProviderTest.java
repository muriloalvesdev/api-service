package br.com.api.provider;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import br.com.api.constants.UserConstantsForTests;
import br.com.api.user.dto.UserDTO;
import br.com.api.user.model.Role;
import br.com.api.user.model.Role.RoleName;
import br.com.api.user.model.User;

public class UserDetailsProviderTest implements ArgumentsProvider, UserConstantsForTests {

  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
    User user = new User(UUID.randomUUID(), FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);
    Role role = new Role(RoleName.ROLE_ADMIN);
    user.setRoles(Set.of(role));
    return Stream.of(UserDTO.build(user)).map(Arguments::of);
  }
}
