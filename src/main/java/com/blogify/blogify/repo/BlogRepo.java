package com.blogify.blogify.repo;

import com.blogify.blogify.model.BlogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepo extends MongoRepository<BlogEntity, String> {
}
