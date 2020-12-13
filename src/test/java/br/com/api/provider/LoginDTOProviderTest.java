package br.com.api.provider;


import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import br.com.api.constants.UserConstantsForTests;
import br.com.api.user.dto.LoginDTO;

public class LoginDTOProviderTest implements ArgumentsProvider, UserConstantsForTests {

  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
    LoginDTO dto = new LoginDTO();
    dto.setEmail(EMAIL);
    dto.setPassword(PASSWORD);
    return Stream.of(dto).map(Arguments::of);
  }

}
