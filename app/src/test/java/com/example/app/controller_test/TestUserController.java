package com.example.app.controller_test;

import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import com.example.app.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.app.utils.TestConstants.BASE_URL_FOR_USER_CONTROLLER;
import static com.example.app.utils.TestConstants.ID;
import static com.example.app.utils.Utils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class TestUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;

    @Test
    public void registrationUserTestPositive() throws Exception {
        assertThat(userRepository.count()).isEqualTo(0);
        utils.regDefaultUser().andExpect(status().isOk());
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    public void getUserPositiveTest() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);
        final MockHttpServletResponse response = utils.perform(
                        get(BASE_URL_FOR_USER_CONTROLLER + ID, expectedUser.getId()),
                        expectedUser.getUsername())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final User user = fromJson(response.getContentAsString(), new TypeReference<>() {

        });

        assertThat(user.getId()).isEqualTo(expectedUser.getId());
        assertThat(user.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(user.getCreatedAt()).isEqualTo(expectedUser.getCreatedAt());
    }

    @Test
    public void getUserNegativeTest() throws Exception {
        utils.regDefaultUser();
        utils.perform(
                        get(BASE_URL_FOR_USER_CONTROLLER))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse();
    }


    @Test
    public void doRegistrationOneUserTwiceTest() throws Exception {
        utils.regDefaultUser().andExpect(status().isOk());
        utils.regIncorrectUser().andExpect(status().isBadRequest());

        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    public void getAuthenticatedPositive() throws Exception {
        utils.regDefaultUser().andExpect(status().isOk());
        utils.authDefaultUser().andExpect(status().isOk());
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    public void getAuthenticatedNegative() throws Exception {
        utils.regDefaultUser().andExpect(status().isOk());
        utils.authIncorrectUser().andExpect(status().isUnauthorized());
    }
}
