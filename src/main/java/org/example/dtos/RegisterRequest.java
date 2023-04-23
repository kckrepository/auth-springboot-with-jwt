package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
    @Pattern(regexp = "^(?=\\d{10,13}$)(08)\\d+")
    @JsonProperty("phone")
    private String phone;
    @NotBlank
    @Size(min=3, max=60)
    @JsonProperty("name")
    private String name;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,16}$")
    @JsonProperty("password")
    private String password;
}