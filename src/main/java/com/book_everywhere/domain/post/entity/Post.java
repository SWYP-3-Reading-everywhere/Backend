package com.book_everywhere.domain.post.entity;

import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.pin.dto.PinRespDto;
import com.book_everywhere.domain.pin.entity.Pin;
import com.book_everywhere.domain.post.dto.PostRespDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<PostImage> postImage = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isPublishing;

    @Transient
    private Long likeCount;

    @Transient
    private boolean likeState;

    public PostRespDto toRespDto(PinRespDto pinRespDto) {
        return new PostRespDto(this.getId(), this.getTitle(), this.getContent(), this.getPostImage().stream().map(PostImage::getImageUrl).toList(), pinRespDto, this.isPublishing());
    }
}
