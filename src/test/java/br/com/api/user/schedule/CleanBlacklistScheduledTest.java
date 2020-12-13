package br.com.api.user.schedule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import br.com.api.config.jwt.JwtBlacklist;

class CleanBlacklistScheduledTest {

  private CleanBlacklistScheduled schedule;
  private JwtBlacklist jwtBlacklist;

  @BeforeEach
  void setUp() {
    this.jwtBlacklist = mock(JwtBlacklist.class);
    this.schedule = new CleanBlacklistScheduled(this.jwtBlacklist);
  }

  @Test
  void cleanBlacklistWithSuccess() {
    // when
    BDDMockito.doNothing().when(this.jwtBlacklist).cleanBlacklist();

    // then
    this.schedule.cleanBlacklist();

    verify(this.jwtBlacklist, times(1)).cleanBlacklist();
  }
}
