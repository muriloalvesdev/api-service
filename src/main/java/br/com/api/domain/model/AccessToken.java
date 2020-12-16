package br.com.api.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {

  @JsonProperty("access_token")
  private String accessToken;

  @Override
  public String toString() {
    return "AccessToken [accessToken=" + accessToken + "]";
  }
}
