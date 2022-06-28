package com.example.app.controller;

import com.example.app.dto.SpecDto;
import com.example.app.dto.UserDto;
import com.example.app.model.Post;
import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import com.example.app.service.Impls.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(UserController.BASE_URL_FOR_USERS_CONTROLLER)
public class UserController {

    public final static String BASE_URL_FOR_USERS_CONTROLLER = "/v1/users";
    public final static String URL_TO_GET_USER_POSTS_HISTORY = "/{id}/history";
    public final static String ID = "/{id}";

    @Autowired
    UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping(ID)
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).get();
    }

    @PostMapping(URL_TO_GET_USER_POSTS_HISTORY)
    public List<Post> getPostHistory(@RequestBody SpecDto specDto) {
        return userService.getHistory(specDto);
    }

    @PostMapping
    public User createUser(@RequestBody @Valid UserDto userDto) {
        return userService.createUser(userDto);
    }

    @DeleteMapping(ID)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
