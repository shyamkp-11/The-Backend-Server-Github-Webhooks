package com.shyampatel.webapp.githubplayroom.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

  @NotBlank
  @Size(min = 2, max = 50)
  private String email;
  @NotBlank
  @Size(min = 6, max = 50)
  String password;
}