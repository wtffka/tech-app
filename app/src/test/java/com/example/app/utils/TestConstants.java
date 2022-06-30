package com.example.app.utils;

import com.example.app.dto.PostDto;
import com.example.app.dto.ShortUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class TestConstants {
    public static final String TEST_USERNAME = "wtffka";
    public static final String TEST_PASSWORD = "111111";
    public static final String TEST_MESSAGE1 = "HIGHWAY TO HELL!";
    public static final String TEST_MESSAGE2 = "ROCK YOU LIKE A HURRICANE!";
    public static final String TEST_MESSAGE3 = "BILLY JEAN!";
    public static final ShortUserDto SHORT_USER_DTO = new ShortUserDto(TEST_USERNAME);
    public static final PostDto POST_DTO1 = new PostDto(TEST_MESSAGE1, SHORT_USER_DTO);
    public static final String BASE_URL_FOR_USER_CONTROLLER = "/v1/users";
    public static final String BASE_URL_FOR_POST_CONTROLLER = "/v1/posts";
    public static final String ID = "/{id}";
    public static final String BASE_URL_FOR_USER_AUTH = "/v1/login";
    public static final ObjectMapper MAPPER = new ObjectMapper();



}
