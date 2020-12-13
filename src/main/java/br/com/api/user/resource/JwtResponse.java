package br.com.api.user.resource;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtResponse {
  private String token;
  private String type = "Bearer";
}
