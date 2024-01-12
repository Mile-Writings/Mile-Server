package com.mile.post.service;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.writerName.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostTemporaryService {
    private final PostRepository postRepository;

    public Long getTemporaryPostExist(
            final Moim moim,
            final WriterName writerName
    ) {
        List<Post> postList = postRepository.findByMoimAndWriterNameWhereIsTemporary(moim, writerName);
        if(isPostListEmpty(postList)) {
            return 0L;
        }
        return postList.stream().sorted(Comparator.comparing(Post::getCreatedAt).reversed()).toList().get(0).getId();
    }

    private boolean isPostListEmpty(
            final List<Post> postList
    ) {
        return postList.isEmpty();
    }
}
