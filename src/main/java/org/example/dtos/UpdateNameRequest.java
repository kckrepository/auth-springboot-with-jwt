package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateNameRequest {
    @NotBlank
    @Size(min=3, max=60)
    @JsonProperty("name")
    private String name;
}
