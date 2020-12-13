package br.com.api.user.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Token {

  @JsonProperty("token")
  private String token;
}
