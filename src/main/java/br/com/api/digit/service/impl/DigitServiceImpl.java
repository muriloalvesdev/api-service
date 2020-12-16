package br.com.api.digit.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import br.com.api.digit.resource.Resource;
import br.com.api.digit.service.DigitSender;
import br.com.api.digit.service.DigitService;
import br.com.api.domain.repository.UniqueDigitRepository;
import br.com.api.dto.UniqueDigitDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Service
class DigitServiceImpl implements DigitService {

  private final UniqueDigitRepository repository;
  private final DigitSender sender;

  @Cacheable("result")
  public List<UniqueDigitDTO> find(String username) {
    List<UniqueDigitDTO> uniqueDigitDTOs = new ArrayList<>();
    this.repository.findByUsername(username.toUpperCase()).forEach(digit -> {
      uniqueDigitDTOs.add(digit.build());
    });
    return uniqueDigitDTOs;
  }

  @Cacheable("result")
  public Resource calculete(String digit, String quantity, String username) {
    return this.sender.calculete(digit, quantity, username);
  }
}
