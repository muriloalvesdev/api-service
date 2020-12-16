package br.com.api.user.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.api.config.jwt.JwtProvider;
import br.com.api.domain.model.AccessToken;
import br.com.api.domain.model.Role;
import br.com.api.domain.model.User;
import br.com.api.domain.model.Role.RoleName;
import br.com.api.domain.repository.RoleRepository;
import br.com.api.domain.repository.UserRepository;
import br.com.api.dto.LoginDTO;
import br.com.api.dto.RegisterDTO;
import br.com.api.dto.UserDTO;
import br.com.api.exception.EmailNotFoundException;
import br.com.api.exception.ExistingEmailException;
import br.com.api.exception.IllegalRoleException;
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

  public void delete(String email) {
    User user = this.findByEmail(email);
    this.userRepository.delete(user);
  }

  public UserDTO find(String email) {
    return UserDTO.build(this.findByEmail(email));
  }

  public List<UserDTO> find() {
    List<UserDTO> dtos = new ArrayList<>();
    this.userRepository.findAll().forEach(user -> {
      dtos.add(UserDTO.build(user));
    });
    return dtos;
  }

  public UserDTO update(RegisterDTO registerDTO) {
    User user = this.findByEmail(registerDTO.getEmail());
    user.setName(registerDTO.getName().toUpperCase());
    user.setLastName(registerDTO.getLastName().toUpperCase());
    user.setPassword(this.encoder.encode(registerDTO.getPassword()));

    Set<String> strRoles = registerDTO.getRole();
    Set<Role> roles = new HashSet<>();

    setRoles(strRoles, roles);
    user.setRoles(roles);

    this.userRepository.saveAndFlush(user);
    return UserDTO.build(user);
  }

  public User register(RegisterDTO registerDTO) {

    if (this.userRepository.existsByEmail(registerDTO.getEmail())) {
      throw new ExistingEmailException(EMAIL_IS_ALREADY);
    }

    User user = new User(UUID.randomUUID(), registerDTO.getName().toUpperCase(),
        registerDTO.getLastName().toUpperCase(), registerDTO.getEmail(),
        this.encoder.encode(registerDTO.getPassword()));

    Set<String> strRoles = registerDTO.getRole();
    Set<Role> roles = new HashSet<>();

    setRoles(strRoles, roles);

    user.setRoles(roles);
    return this.userRepository.saveAndFlush(user);
  }

  private void setRoles(Set<String> strRoles, Set<Role> roles) {
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

  private User findByEmail(String email) {
    return this.userRepository.findByEmail(email)
        .orElseThrow(() -> new EmailNotFoundException(EMAIL_NOT_FOUND));
  }
}
