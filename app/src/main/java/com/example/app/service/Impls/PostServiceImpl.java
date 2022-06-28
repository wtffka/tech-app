package com.example.app.service.Impls;

import com.example.app.dto.PostDto;
import com.example.app.dto.SpecDto;
import com.example.app.model.Post;
import com.example.app.repository.PostRepository;
import com.example.app.repository.UserRepository;
import com.example.app.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Post createPost(PostDto postDto) {
        final Post post = new Post();
        setPostData(postDto, post);
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(PostDto postDto, Long id) {
        final Post postToUpdate = postRepository.findById(id).orElseThrow(NoSuchElementException::new);
        setPostData(postDto, postToUpdate);
        return postRepository.save(postToUpdate);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.delete(postRepository.findById(id).get());
    }


    private void setPostData(PostDto postDto, Post post) {
        post.setMessage(postDto.getMessage());
        post.setUser(userRepository.findByUsername(postDto.getShortUserDto().getUsername()).get());
    }
}
