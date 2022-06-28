package com.example.app.service.Impls;

import com.example.app.dto.SpecDto;
import com.example.app.dto.UserDto;
import com.example.app.model.Post;
import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import com.example.app.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    public static final List<GrantedAuthority> DEFAULT_AUTHORITIES = List.of(new SimpleGrantedAuthority("USER"));

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserDto userDto) {
        final User user = new User();
        setUserData(userDto, user);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findById(id).get());
    }

    @Override
    public List<Post> getHistory(SpecDto specDto) {
        List<Post> posts = userRepository.
                findByUsername(specDto.getUsername()).
                orElseThrow(NoSuchElementException::new)
                .getPosts();
        if (parseSpecDtoMessage(specDto.getMessage()) > posts.size()) {
            return posts;
        }
        return posts.subList(0, parseSpecDtoMessage(specDto.getMessage()));
    }

    private Integer parseSpecDtoMessage(String message) {
        if (StringUtils.isNumeric(Arrays.stream(message.split(" ")).toList().get(1))) {
            return Integer.parseInt(Arrays.stream(message.split(" ")).toList().get(1));
        }
        throw new IllegalArgumentException("Impossible to get History of Messages.");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::buildSpringUser)
                .orElseThrow(() -> new UsernameNotFoundException("User with that username not found"));
    }

    private void setUserData(UserDto userDto, User user) {
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }

    private UserDetails buildSpringUser(final User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                DEFAULT_AUTHORITIES
        );
    }
}
