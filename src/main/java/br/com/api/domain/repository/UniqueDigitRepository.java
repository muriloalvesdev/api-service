package br.com.api.domain.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.api.domain.model.UniqueDigit;

public interface UniqueDigitRepository extends JpaRepository<UniqueDigit, UUID> {
  List<UniqueDigit> findByUsername(String username);
  Boolean existsUsername(String username);
}
