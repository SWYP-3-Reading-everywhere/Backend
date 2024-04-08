package com.book_everywhere.common.auth.entity;

import com.book_everywhere.common.entity.BaseTimeEntity;
import com.book_everywhere.domain.review.entity.Review;
import com.book_everywhere.domain.tag.entity.Tagged;
import com.book_everywhere.domain.pin.entity.Visit;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Visit> visits = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Tagged> taggeds = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private Long socialId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    private Role role;

    public User update(String nickname, String image, Role role) {
        this.nickname = nickname;
        this.image = image;
        this.role = role;
        return this;
    }
}
