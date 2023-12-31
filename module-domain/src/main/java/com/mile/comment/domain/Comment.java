package com.mile.comment.domain;

import com.mile.config.BaseTimeEntity;
import com.mile.post.domain.Post;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.user.domain.User;
import com.mile.writerName.domain.WriterName;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Post post;

    private String content;
    private boolean anonymous;

    @ManyToOne
    private WriterName writerName;

    public static Comment create(
            final Post post,
            final WriterName writerName,
            final CommentCreateRequest createRequest,
            final boolean anonymous
    ) {
        return Comment
                .builder()
                .post(post)
                .content(createRequest.content())
                .writerName(writerName)
                .anonymous(anonymous)
                .build();
    }
}
