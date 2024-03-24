package com.mile.comment.domain;

import com.mile.config.BaseTimeEntity;
import com.mile.post.domain.Post;
import com.mile.post.service.dto.CommentCreateRequest;
import com.mile.writername.domain.WriterName;
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
import lombok.Setter;

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
    @Setter
    private String idUrl;
    private String content;
    private boolean anonymous;

    @ManyToOne
    private WriterName writerName;

    public static Comment create(
            final Post post,
            final WriterName writerName,
            final CommentCreateRequest createRequest
    ) {
        return Comment
                .builder()
                .post(post)
                .content(createRequest.content())
                .writerName(writerName)
                .anonymous(createRequest.isAnonymous())
                .build();
    }

}
