package com.mile.curious.repository.dto;

import com.mile.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostAndCuriousCountInLastWeek implements Comparable<PostAndCuriousCountInLastWeek> {
    private Post post;
    private Long count;

    @Override
    public int compareTo(final PostAndCuriousCountInLastWeek target) {
        return Long.compare(this.count, target.count);
    }
}
