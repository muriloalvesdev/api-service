package br.com.api.dto;

import java.util.Set;
import javax.validation.constraints.Email;
import org.springframework.lang.NonNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterDTO {

  @NonNull
  private String name;

  @NonNull
  @JsonProperty("lastname")
  private String lastName;

  @Email
  private String email;
  
  @NonNull
  private Set<String> role;
  
  @NonNull
  private String password;

  public RegisterDTO(String name, String lastName, @Email String email, Set<String> role,
      String password) {
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.role = role;
    this.password = password;
  }

  public RegisterDTO() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<String> getRole() {
    return this.role;
  }

  public void setRole(Set<String> role) {
    this.role = role;
  }

  @Override
  public String toString() {
    return "RegisterData [name=" + name + ", lastName=" + lastName + ", email=" + email + ", role="
        + role + ", password=" + password + "]";
  }
}
