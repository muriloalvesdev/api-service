package br.com.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDTO {

  private String email;
  private String password;

  @Override
  public String toString() {
    return "LoginData [email=" + email + ", password=" + password + "]";
  }
}
