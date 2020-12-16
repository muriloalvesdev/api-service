package br.com.api.digit.service;

import java.util.List;
import br.com.api.digit.resource.Resource;
import br.com.api.dto.UniqueDigitDTO;

public interface DigitService {
  List<UniqueDigitDTO> find(String username);

  Resource calculete(String digit, String quantity, String username);
}
