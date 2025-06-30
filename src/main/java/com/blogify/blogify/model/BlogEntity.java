package com.blogify.blogify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "blogs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogEntity {

        @Id
        private String id;
        @Indexed(unique = true)

        private String title;

        private String content;

        @NonNull
        private String authorId;

        private String authorName;

        private LocalDateTime createdAt = LocalDateTime.now();

        private LocalDateTime updatedAt;
}
