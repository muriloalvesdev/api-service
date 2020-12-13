package br.com.api.user.service;

import org.springframework.stereotype.Service;
import br.com.api.config.jwt.JwtBlacklist;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Service
public class InvalidationTokenService {

  private JwtBlacklist blacklist;

  public void invalidate(String token) {
    blacklist.add(token);
  }
}
