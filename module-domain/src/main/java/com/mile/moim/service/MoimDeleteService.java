package com.mile.moim.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.service.CommentService;
import com.mile.commentreply.service.CommentReplyService;
import com.mile.curious.service.CuriousService;
import com.mile.moim.domain.Moim;
import com.mile.moim.repository.MoimRepository;
import com.mile.post.domain.Post;
import com.mile.post.service.PostDeleteService;
import com.mile.post.service.PostGetService;
import com.mile.topic.domain.Topic;
import com.mile.topic.service.TopicService;
import com.mile.writername.service.WriterNameDeleteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MoimDeleteService {

    private final MoimService moimService;
    private final TopicService topicService;
    private final PostGetService postGetService;
    private final CommentReplyService commentReplyService;
    private final PostDeleteService postDeleteService;
    private final CommentService commentService;
    private final CuriousService curiousService;
    private final MoimRepository moimRepository;
    private final WriterNameDeleteService writerNameDeleteService;

    @Transactional
    public void deleteMoim(
            final Long moimId,
            final Long userId
    ) {
        moimService.getAuthenticateOwnerOfMoim(moimId, userId);
        Moim moim = moimService.findById(moimId);
        List<Topic> topics = topicService.findTopicListByMoimId(moimId);
        List<Post> posts = postGetService.findAllByTopics(topics);
        List<Comment> comments = commentService.findAllByPosts(posts);

        commentReplyService.deleteRepliesByComments(comments);
        commentService.deleteComments(comments);
        curiousService.deleteAllByPosts(posts);
        postDeleteService.deletePosts(posts);
        topicService.deleteTopics(topics);
        writerNameDeleteService.deleteWriterNamesByMoim(moim);
        moimRepository.delete(moim);

    }
}
