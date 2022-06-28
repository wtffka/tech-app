package com.example.app.utils;

import com.example.app.dto.PostDto;
import com.example.app.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

import static com.example.app.utils.TestConstants.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@Component
public class Utils {

    private final UserDto testUserDto = new UserDto(
            TEST_USERNAME,
            TEST_PASSWORD
    );

    private final PostDto testPostDto = new PostDto(
            TEST_MESSAGE1,
            SHORT_USER_DTO
    );

    private final PostDto testPostDtoEmpty = new PostDto(

    );

    private final UserDto testUserDtoEmpty = new UserDto();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTHelper jwtHelper;

    public ResultActions regDefaultUser() throws Exception {
        return regUser(testUserDto);
    }


    public ResultActions regTestPostDto1() throws Exception {
        return regPost(testPostDto);
    }

    public ResultActions regIncorrectPost() throws Exception {
        return regPost(testPostDtoEmpty);
    }

    public ResultActions regIncorrectUser() throws Exception {
        return regUser(testUserDtoEmpty);
    }

    public ResultActions authDefaultUser() throws Exception {
        return authUser(testUserDto);
    }

    public ResultActions authIncorrectUser() throws Exception {
        return authUser(testUserDtoEmpty);
    }

    public ResultActions regUser(final UserDto userDto) throws Exception {
        final MockHttpServletRequestBuilder request = post(BASE_URL_FOR_USER_CONTROLLER)
                .content(toJson(userDto))
                .contentType(MediaType.APPLICATION_JSON);
        return perform(request);
    }

    public ResultActions regPost(final PostDto postDto) throws Exception {
        MockHttpServletRequestBuilder request = post(BASE_URL_FOR_POST_CONTROLLER)
                .content(toJson(postDto))
                .contentType(MediaType.APPLICATION_JSON);

        return perform(request);
    }

    public ResultActions authUser(final UserDto userDto) throws Exception {
        final MockHttpServletRequestBuilder request = post(BASE_URL_FOR_USER_AUTH)
                .content(toJson(userDto))
                .contentType(MediaType.APPLICATION_JSON);

        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request, final String username) throws Exception {
        final String token = jwtHelper.expiring(Map.of("email", username));
        request.header(AUTHORIZATION, token);
        return perform(request);
    }

    public static String toJson(Object o) throws JsonProcessingException {
        return MAPPER.findAndRegisterModules().writeValueAsString(o);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> type) throws JsonProcessingException {
        return MAPPER.findAndRegisterModules().readValue(json, type);
    }
}