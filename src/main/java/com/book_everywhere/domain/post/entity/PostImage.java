package com.book_everywhere.domain.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class PostImage {
    @Id
    @GeneratedValue
    @Column(name = "post_image_id")
    private Long id;

    private String imageUrl;
}
