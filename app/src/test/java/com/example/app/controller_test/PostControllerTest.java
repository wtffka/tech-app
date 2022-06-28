package com.example.app.controller_test;

import com.example.app.model.Post;
import com.example.app.model.User;
import com.example.app.repository.PostRepository;
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

import static com.example.app.utils.TestConstants.*;
import static com.example.app.utils.Utils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class PostControllerTest {

    @Autowired
    private Utils utils;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void registrationPostTestPositive() throws Exception {
        assertThat(postRepository.count()).isEqualTo(0);
        utils.regDefaultUser();
        utils.regTestPostDto1();
        assertThat(postRepository.count()).isEqualTo(1);
    }

    @Test
    public void registrationPostTestNegative() throws Exception {
        assertThat(postRepository.count()).isEqualTo(0);
        utils.regDefaultUser().andExpect(status().isOk());
        assertThat(postRepository.count()).isEqualTo(1);
    }

    @Test
    public void getPostPositiveTest() throws Exception {
        utils.regDefaultUser();
        utils.regTestPostDto1();
        final Post expectedPost = postRepository.findAll().get(0);
        final MockHttpServletResponse response = utils.perform(
                        get(BASE_URL_FOR_POST_CONTROLLER + ID, expectedPost.getId()),
                        expectedPost.getMessage())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Post post = fromJson(response.getContentAsString(), new TypeReference<>() {

        });

        assertThat(post.getId()).isEqualTo(expectedPost.getId());
        assertThat(post.getMessage()).isEqualTo(expectedPost.getMessage());
        assertThat(post.getUser()).isEqualTo(expectedPost.getUser());
        assertThat(post.getCreatedAt()).isEqualTo(expectedPost.getCreatedAt());
    }

    @Test
    public void getUserNegativeTest() throws Exception {
        utils.regDefaultUser();
        utils.regIncorrectPost();
        final MockHttpServletResponse response = utils.perform(
                        get(BASE_URL_FOR_POST_CONTROLLER))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<User> users = fromJson(response.getContentAsString(), new TypeReference<>() {

        });

        assertThat(users.size()).isEqualTo(1);

    }

    @Test
    public void doRegistrationOnePostTwiceTest() throws Exception {
        utils.regTestPostDto1().andExpect(status().isOk());
        utils.regTestPostDto1().andExpect(status().isOk());

        assertThat(userRepository.count()).isEqualTo(2);
    }

}
