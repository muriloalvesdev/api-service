package br.com.api.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.api.domain.model.Role;
import br.com.api.domain.model.Role.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(RoleName roleName);
}
