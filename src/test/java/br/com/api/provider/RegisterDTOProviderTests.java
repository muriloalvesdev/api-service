package br.com.api.provider;


import java.util.HashSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import br.com.api.constants.UserConstantsForTests;
import br.com.api.user.dto.RegisterDTO;

public class RegisterDTOProviderTests implements ArgumentsProvider, UserConstantsForTests {


  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
    RegisterDTO dto = new RegisterDTO();
    HashSet<String> roles = new HashSet<>();
    roles.add("admin");
    dto.setEmail(EMAIL);
    dto.setName(FIRST_NAME);
    dto.setPassword(PASSWORD);
    dto.setRole(roles);
    dto.setLastName(LAST_NAME);
    return Stream.of(dto).map(Arguments::of);
  }
}
