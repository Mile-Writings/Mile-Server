package com.mile.curious.domain;

import com.mile.common.config.BaseTimeEntity;
import com.mile.post.domain.Post;
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

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Curious extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Post post;

    @ManyToOne
    private WriterName writerName;

    public static Curious create(
            final Post post,
            final WriterName writerName
    ) {
        return Curious
                .builder()
                .post(post)
                .writerName(writerName)
                .build();
    }

}



