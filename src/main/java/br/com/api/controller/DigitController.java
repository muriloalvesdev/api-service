package br.com.api.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.api.digit.resource.Resource;
import br.com.api.digit.service.DigitService;
import br.com.api.dto.UniqueDigitDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@RestController
@RequestMapping("api/digit")
class DigitController {

  private final DigitService service;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("{digit}/{quantity}/{username}")
  public ResponseEntity<Resource> convert(@PathVariable(name = "digit") String digit,
      @PathVariable(name = "quantity", required = false) String quantity,
      @PathVariable(name = "username") String username) {
    return ResponseEntity.ok(this.service.calculete(digit, quantity, username));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("{username}")
  public ResponseEntity<List<UniqueDigitDTO>> find(
      @PathVariable(name = "username") String username) {
    return ResponseEntity.ok(this.service.find(username));
  }
}
