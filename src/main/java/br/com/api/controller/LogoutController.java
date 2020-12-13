package br.com.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.api.user.resource.Token;
import br.com.api.user.service.InvalidationTokenService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class LogoutController {

  private InvalidationTokenService service;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(path = "/token-expiration/")
  public HttpStatus setExpirationToken(@RequestBody Token token) {
    this.service.invalidate(token.getToken());
    return HttpStatus.NO_CONTENT;
  }
}
