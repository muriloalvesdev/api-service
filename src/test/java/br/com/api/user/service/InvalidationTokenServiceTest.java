package br.com.api.user.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import br.com.api.config.jwt.JwtBlacklist;
import br.com.api.constants.UserConstantsForTests;

class InvalidationTokenServiceTest implements UserConstantsForTests {

  private InvalidationTokenService service;
  private JwtBlacklist blacklist;

  @BeforeEach
  void setUp() {
    this.blacklist = mock(JwtBlacklist.class);
    this.service = new InvalidationTokenService(this.blacklist);
  }

  @Test
  void invalidateTest() {
    // given (token is it given)

    // when
    BDDMockito.doNothing().when(this.blacklist).add(TOKEN);

    // then
    this.service.invalidate(TOKEN);

    verify(this.blacklist, times(1)).add(Mockito.anyString());
  }
}
