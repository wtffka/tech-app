package com.example.app.service;

import com.example.app.dto.SpecDto;
import com.example.app.dto.UserDto;
import com.example.app.model.Post;
import com.example.app.model.User;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto);
    void deleteUser(Long id);
    List<Post> getHistory(SpecDto specDto);
}
