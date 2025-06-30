package com.blogify.blogify.service;

import com.blogify.blogify.model.BlogEntity;
import com.blogify.blogify.model.UserEntity;
import com.blogify.blogify.repo.BlogRepo;
import com.blogify.blogify.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogRepo repo;

    @Autowired
    private UserRepo uRepo;

    public List<BlogEntity> getAllPosts() {
        return repo.findAll();
    }

    public BlogEntity createPost(BlogEntity blogPost, String email) {
        Optional<UserEntity> userOpt = uRepo.findByEmail(email);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            blogPost.setAuthorId(user.getId());
            blogPost.setAuthorName(user.getName());
            blogPost.setCreatedAt(LocalDateTime.now());
            return repo.save(blogPost);
        } else {
            throw new RuntimeException("User not found: " + email);
        }
    }


    public Optional<BlogEntity> getPostById(String id) {
        return repo.findById(id);
    }

    public BlogEntity updatePost(String id, BlogEntity updatedPost, String email) {
        BlogEntity existingPost = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));

        // Fetch current user from DB by email
        Optional<UserEntity> userOpt = uRepo.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }

        UserEntity user = userOpt.get();

        // Check if the current user is the owner of the post
        if (!existingPost.getAuthorId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this post.");
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());

        return repo.save(existingPost);
    }


    public void deletePost(String postId, String email) {
        BlogEntity post = repo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        Optional<UserEntity> userOpt = uRepo.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }

        UserEntity user = userOpt.get();

        if (!post.getAuthorId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this post.");
        }

        repo.deleteById(postId);
    }
}
