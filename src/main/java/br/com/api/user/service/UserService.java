package br.com.api.user.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.api.config.jwt.JwtProvider;
import br.com.api.exception.EmailNotFoundException;
import br.com.api.exception.ExistingEmailException;
import br.com.api.exception.IllegalRoleException;
import br.com.api.user.dto.LoginDTO;
import br.com.api.user.dto.RegisterDTO;
import br.com.api.user.model.AccessToken;
import br.com.api.user.model.Role;
import br.com.api.user.model.Role.RoleName;
import br.com.api.user.model.User;
import br.com.api.user.repository.RoleRepository;
import br.com.api.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Service
public class UserService {

  static final String EMAIL_IS_ALREADY = "Fail -> Email is already in use!";
  static final String ROLE_NOT_FOUND = "Fail! -> Cause: %s Role not find.";
  static final String ROLE_INVALID = "Fail! -> Cause: Role invalid.";
  static final String EMAIL_NOT_FOUND = "Informed email does not exist in the database!";

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder encoder;
  private final JwtProvider jwtProvider;
  private final AuthenticationManager authenticationManager;

  public User registerUser(RegisterDTO registerDTO) {

    if (this.userRepository.existsByEmail(registerDTO.getEmail())) {
      throw new ExistingEmailException(EMAIL_IS_ALREADY);
    }

    User user = new User(UUID.randomUUID(), registerDTO.getName(), registerDTO.getLastName(),
        registerDTO.getEmail(), this.encoder.encode(registerDTO.getPassword()));

    Set<String> strRoles = registerDTO.getRole();
    Set<Role> roles = new HashSet<>();

    strRoles.forEach(role -> {
      switch (role.toLowerCase()) {
        case "admin":
          Role admin = this.roleRepository.findByName(RoleName.ROLE_ADMIN)
              .orElseThrow(() -> new IllegalRoleException(String.format(ROLE_NOT_FOUND, "Admin")));
          roles.add(admin);
          break;
        default:
          throw new IllegalRoleException(ROLE_INVALID);
      }
    });

    user.setRoles(roles);
    return this.userRepository.saveAndFlush(user);
  }

  public AccessToken authenticateUser(LoginDTO loginDto) {
    if (!this.userRepository.existsByEmail(loginDto.getEmail())) {
      throw new EmailNotFoundException(EMAIL_NOT_FOUND);
    }
    Authentication authentication = this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return new AccessToken(this.jwtProvider.generateJwtToken(authentication));
  }
}
