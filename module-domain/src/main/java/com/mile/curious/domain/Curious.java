package com.mile.curious.domain;

import com.mile.post.domain.Post;
import com.mile.config.BaseTimeEntity;
import com.mile.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Curious extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;

    public static Curious create(
            final Post post,
            final User user
    ) {
        return Curious
                .builder()
                .post(post)
                .user(user)
                .build();
    }

}



