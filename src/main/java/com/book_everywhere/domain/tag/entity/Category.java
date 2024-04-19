package com.book_everywhere.domain.tag.entity;

import com.book_everywhere.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "category")
    private List<Tag> tag = new ArrayList<>();

    private String name;

}
