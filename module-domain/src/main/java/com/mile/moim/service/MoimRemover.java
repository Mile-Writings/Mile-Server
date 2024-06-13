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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class MoimRemover {

    private final TopicService topicService;
    private final PostGetService postGetService;
    private final CommentReplyService commentReplyService;
    private final PostDeleteService postDeleteService;
    private final CommentService commentService;
    private final CuriousService curiousService;
    private final MoimRepository moimRepository;
    private final WriterNameDeleteService writerNameDeleteService;
    private final MoimRetriever moimRetriever;

    @Transactional
    public void deleteMoim(
            final Long moimId,
            final Long userId
    ) {
        moimRetriever.getAuthenticateOwnerOfMoim(moimId, userId);
        Moim moim = moimRetriever.findById(moimId);
        List<Topic> topics = topicService.findTopicListByMoimId(moimId);
        List<Post> posts = postGetService.findAllByTopics(topics);
        List<Comment> comments = commentService.findAllByPosts(posts);

        commentReplyService.deleteRepliesByComments(comments);
        commentService.deleteComments(comments);
        curiousService.deleteAllByPosts(posts);
        postDeleteService.deletePosts(posts);
        topicService.deleteTopics(topics);
        moimRepository.delete(moim);

    }
}
