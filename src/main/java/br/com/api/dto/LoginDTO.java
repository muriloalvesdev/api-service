package br.com.api.dto;

import org.springframework.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDTO {

  @NonNull
  private String email;
  @NonNull
  private String password;

  @Override
  public String toString() {
    return "LoginData [email=" + email + ", password=" + password + "]";
  }
}
