package com.example.app.service;

import com.example.app.dto.PostDto;
import com.example.app.dto.SpecDto;
import com.example.app.model.Post;

import java.util.List;

public interface PostService {

    Post createPost(PostDto postDto);
    Post updatePost(PostDto postDto, Long id);
    void deletePost(Long id);
}
