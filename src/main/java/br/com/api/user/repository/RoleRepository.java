package br.com.api.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.api.user.model.Role;
import br.com.api.user.model.Role.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(RoleName roleName);
}
