package com.book_everywhere.domain.pin.entity;

import com.book_everywhere.common.entity.BaseTimeEntity;
import com.book_everywhere.domain.review.entity.Review;
import com.book_everywhere.domain.tag.entity.Tagged;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Pin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pin_id")
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    private List<Visit> visits = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "pin")
    private List<Review> reviews = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    private List<Tagged> tags = new ArrayList<>();

    @Column(nullable = false)
    private double placeId;

    //경도 x
    @Column(nullable = false)
    private double latitude;
    //위도 y
    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;
    @Column(nullable = false,unique = true)
    private String address;

    @Column
    private String phone;

}
