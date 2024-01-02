package com.mile.comment.domain;

import com.mile.config.BaseTimeEntity;
import com.mile.post.domain.Post;
import com.mile.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Post post;

    private String content;
    private boolean anonymous;

    @ManyToOne
    private User user;
}
