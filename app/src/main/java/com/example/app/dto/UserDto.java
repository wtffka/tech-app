package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank
    @Size(min = 3)
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;
}
