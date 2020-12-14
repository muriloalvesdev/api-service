package br.com.api.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.api.user.dto.LoginDTO;
import br.com.api.user.dto.RegisterDTO;
import br.com.api.user.model.AccessToken;
import br.com.api.user.model.User;
import br.com.api.user.service.UserService;
import lombok.AllArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

  private UserService userService;

  @PostMapping("/login")
  public ResponseEntity<AccessToken> authenticateUser(@Validated @RequestBody LoginDTO loginData) {
    return ResponseEntity.ok().body(this.userService.authenticateUser(loginData));
  }

  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterDTO registerData) {
    return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(this.userService.registerUser(registerData).getId()).toUri()).build();
  }
}
