package com.book_everywhere.domain.review.entity;

import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.book.entity.Book;
import com.book_everywhere.common.entity.BaseTimeEntity;
import com.book_everywhere.domain.pin.entity.Pin;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 1500)
    private String content;
    @Column(nullable = false)
    private boolean isPrivate;
    @Column(nullable = false)
    private String writer;
    @Column(nullable = false)
    private boolean isBookComplete;

    @Transient // 칼럼이 만들어지지 않는다.
    private Long likeCount;

    @Transient
    private boolean likeState;


  //==연관 관계 편의 메서드==//
    public void setBook(Book book) {
        if(this.book != null) {
            this.book.getReviews().remove(this);
        }
        this.book = book;
        book.getReviews().add(this);
    }

    public void setUser(User user) {
        if(this.user != null) {
            this.user.getReviews().remove(this);
        }
        this.user = user;
        user.getReviews().add(this);
    }

    public void setPin(Pin pin) {
        if(this.pin != null) {
            this.pin.getReviews().remove(this);
        }
        this.pin = pin;
        pin.getReviews().add(this);
    }

    //==수정 메서드==//
    public void changeReview(String title, String content, boolean isPrivate, boolean isBookComplete, String writer) {
        this.title = title;
        this.content = content;
        this.isPrivate = isPrivate;
        this.isBookComplete = isBookComplete;
        this.writer = writer;
    }
}
