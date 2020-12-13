package br.com.api.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import br.com.api.exception.EmailNotFoundException;
import br.com.api.user.dto.UserDTO;
import br.com.api.user.model.User;
import br.com.api.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class UserDetailsServiceImpl implements UserDetailsService {

  static final String EMAIL_NOT_FOUND = "User Not Found with -> email : %s";
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) {

    User user = this.userRepository.findByEmail(email)
        .orElseThrow(() -> new EmailNotFoundException(String.format(EMAIL_NOT_FOUND, email)));

    return UserDTO.build(user);
  }
}
