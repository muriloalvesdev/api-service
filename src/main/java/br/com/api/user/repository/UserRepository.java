package br.com.api.user.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.api.user.model.TokenEntity;
import br.com.api.user.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

  Optional<User> findByToken(TokenEntity tokenEntity);
}
