package com.example.app.dto;

import com.example.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    @NotBlank
    private String message;

    @NotBlank
    private ShortUserDto shortUserDto;

}
