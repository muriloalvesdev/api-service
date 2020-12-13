package br.com.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.api.digit.resource.Resource;
import br.com.api.digit.service.DigitSender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@RestController
@RequestMapping("api/digit")
class DigitController {

  private final DigitSender sender;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("{digit}/{quantity}")
  public ResponseEntity<Resource> convert(@PathVariable(name = "digit") String digit,
      @PathVariable(name = "quantity", required = false) String quantity) {
    return ResponseEntity.ok(this.sender.calculete(digit, quantity));
  }
}
