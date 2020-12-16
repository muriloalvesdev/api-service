package br.com.api.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.api.domain.model.AccessToken;
import br.com.api.domain.model.User;
import br.com.api.dto.LoginDTO;
import br.com.api.dto.RegisterDTO;
import br.com.api.dto.UserDTO;
import br.com.api.user.service.UserService;
import lombok.AllArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
@AllArgsConstructor
class UserController {

  private UserService userService;

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("delete{email}")
  public void delete(@RequestParam(name = "email") String email) {
    this.userService.delete(email);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/update")
  public ResponseEntity<UserDTO> update(@RequestBody RegisterDTO registerDTO) {
    return ResponseEntity.ok(this.userService.update(registerDTO));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/find")
  public ResponseEntity<List<UserDTO>> find() {
    return ResponseEntity.ok(this.userService.find());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/find{email}")
  public ResponseEntity<UserDTO> find(@RequestParam(name = "email") String email) {
    return ResponseEntity.ok(this.userService.find(email));
  }

  @PostMapping("/login")
  public ResponseEntity<AccessToken> authenticateUser(@Validated @RequestBody LoginDTO loginData) {
    return ResponseEntity.ok().body(this.userService.authenticateUser(loginData));
  }

  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterDTO registerData) {
    return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(this.userService.register(registerData).getId()).toUri()).build();
  }
}
