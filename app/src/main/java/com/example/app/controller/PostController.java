package com.example.app.controller;

import com.example.app.dto.PostDto;
import com.example.app.model.Post;
import com.example.app.repository.PostRepository;
import com.example.app.service.Impls.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.example.app.controller.PostController.BASE_URL_FOR_POST_CONTROLLER;
@RestController
@RequestMapping(BASE_URL_FOR_POST_CONTROLLER)
public class PostController {

    public final static String BASE_URL_FOR_POST_CONTROLLER = "/v1/posts";
    public final static String ID = "/{id}";

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostServiceImpl postService;

    @GetMapping
    public Iterable<Post> getPosts() {
        return postRepository.findAll();
    }

    @GetMapping(ID)
    public Post getPost(@PathVariable Long id) {
        return postRepository.findById(id).get();
    }

    @PostMapping
    public Post createPost(@RequestBody PostDto postDto) {
        return postService.createPost(postDto);
    }

    @PutMapping(ID)
    public Post updatePost(@RequestBody PostDto postDto, @PathVariable Long id) {
        return postService.updatePost(postDto, id);
    }

    @DeleteMapping(ID)
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
