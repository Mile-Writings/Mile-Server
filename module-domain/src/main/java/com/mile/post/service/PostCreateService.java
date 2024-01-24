package com.mile.post.service;

import com.mile.moim.domain.Moim;
import com.mile.post.domain.Post;
import com.mile.post.repository.PostRepository;
import com.mile.writerName.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCreateService {
    private final PostRepository postRepository;

    public String getTemporaryPostExist(
            final Moim moim,
            final WriterName writerName
    ) {
        List<Post> postList = postRepository.findByMoimAndWriterNameWhereIsTemporary(moim, writerName);
        if(isPostListEmpty(postList)) {
            return Base64.getUrlEncoder().encodeToString(String.valueOf(0L).getBytes());
        }
        return postList.stream().sorted(Comparator.comparing(Post::getCreatedAt).reversed()).toList().get(0).getIdUrl();
    }

    private boolean isPostListEmpty(
            final List<Post> postList
    ) {
        return postList.isEmpty();
    }
}
