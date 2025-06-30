package com.blogify.blogify.controller;

import com.blogify.blogify.model.BlogEntity;
import com.blogify.blogify.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/blogs")
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class BlogController {

    @Autowired
    private BlogService service;

    @GetMapping
    public ResponseEntity<List<BlogEntity>> getAllPosts() {
        return ResponseEntity.ok(service.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogEntity> getPostById(@PathVariable String id) {

        Optional<BlogEntity> post = service.getPostById(id);
        if (post.isPresent()) {
            return new ResponseEntity<>(post.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<BlogEntity> createPost(@RequestBody BlogEntity post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println(email);
        BlogEntity savedPost = service.createPost(post, email);
        return ResponseEntity.ok(savedPost);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BlogEntity> updatePost(
            @PathVariable String id,
            @RequestBody BlogEntity post) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Extract the authenticated user's email

        BlogEntity result = service.updatePost(id, post, email);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // get logged-in user's email

        try {
            service.deletePost(id, email);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
    }
}
