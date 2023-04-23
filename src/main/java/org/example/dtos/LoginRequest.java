package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank
    @JsonProperty("phone")
    private String phone;

    @NotBlank
    @JsonProperty("password")
    private String password;
}
