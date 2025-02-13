package com.shyampatel.webapp.githubplayroom.auth;

import com.shyampatel.webapp.githubplayroom.user.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


public class RegisterRequest {

  private String firstname;
  private String lastname;
  @NotBlank
  @Size(min = 2, max = 50)
  private String email;
  @NotBlank
  @Size(min = 6, max = 50)
  private String password;
  @NotNull
  private Role role;

  public RegisterRequest(String firstname, String lastname, String email, String password, Role role) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public RegisterRequest() {
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
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

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    RegisterRequest that = (RegisterRequest) o;
    return Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && role == that.role;
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstname, lastname, email, password, role);
  }

  public static class Builder {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;

    public Builder setFirstname(String firstname) {
      this.firstname = firstname;
      return this;
    }

    public Builder setLastname(String lastname) {
      this.lastname = lastname;
      return this;
    }

    public Builder setEmail(String email) {
      this.email = email;
      return this;
    }

    public Builder setPassword(String password) {
      this.password = password;
      return this;
    }

    public Builder setRole(Role role) {
      this.role = role;
      return this;
    }

    public RegisterRequest build() {
      return new RegisterRequest(firstname, lastname, email, password, role);
    }
  }
}