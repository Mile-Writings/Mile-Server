package com.mile.commentreply.domain;

import com.mile.comment.domain.Comment;
import com.mile.writername.domain.WriterName;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class CommentReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private WriterName writerName;
    @ManyToOne
    private Comment comment;
    @Setter
    private String idUrl;
    private boolean isAnonymous;
    private String content;

    @Builder
    private CommentReply(final WriterName writerName,
                         final Comment comment,
                         final boolean isAnonymous,
                         final String content) {
        this.writerName = writerName;
        this.comment = comment;
        this.isAnonymous = isAnonymous;
        this.content = content;
    }

    public static CommentReply create(final WriterName writerName,
                                      final Comment comment,
                                      final String content,
                                      final boolean isAnonymous) {
        return CommentReply.builder()
                .writerName(writerName)
                .comment(comment)
                .isAnonymous(isAnonymous)
                .content(content)
                .build();
    }
}
