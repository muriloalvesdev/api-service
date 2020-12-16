package br.com.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UniqueDigitDTO {
  private String username;
  private String digit;
  private String quantity;
  private String uniqueDigit;
}
