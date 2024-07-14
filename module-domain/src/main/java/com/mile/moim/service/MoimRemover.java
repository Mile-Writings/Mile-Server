package com.mile.moim.service;

import com.mile.comment.domain.Comment;
import com.mile.comment.service.CommentRemover;
import com.mile.comment.service.CommentRetriever;
import com.mile.commentreply.service.CommentReplyRemover;
import com.mile.curious.service.CuriousRemover;
import com.mile.moim.domain.Moim;
import com.mile.moim.repository.MoimRepository;
import com.mile.post.domain.Post;
import com.mile.post.service.PostRemover;
import com.mile.post.service.PostRetriever;
import com.mile.topic.domain.Topic;
import com.mile.topic.service.TopicRemover;
import com.mile.topic.service.TopicRetriever;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.WriterNameRemover;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MoimRemover {

    private final MoimRepository moimRepository;
    private final MoimRetriever moimRetriever;
    private final WriterNameRemover writerNameRemover;
    private final TopicRetriever topicRetriever;
    private final PostRetriever postRetriever;
    private final CommentRetriever commentRetriever;
    private final CommentReplyRemover commentReplyRemover;
    private final CommentRemover commentRemover;
    private final CuriousRemover curiousRemover;
    private final PostRemover postRemover;
    private final TopicRemover topicRemover;

    public void deleteMoim(
            final Moim moim
    ) {
        moimRepository.delete(moim);
    }

    public void deleteRelatedData(final Moim moim) {

        List<Topic> topics = topicRetriever.findTopicListByMoim(moim);
        List<Post> posts = postRetriever.findAllByTopics(topics);
        List<Comment> comments = commentRetriever.findAllByPosts(posts);
        commentReplyRemover.deleteRepliesByComments(comments);
        commentRemover.deleteComments(posts);
        curiousRemover.deleteAllByPosts(posts);
        postRemover.deletePostsByTopic(topics);
        topicRemover.deleteTopicsByMoim(moim);
        writerNameRemover.deleteWriterNamesByMoim(moim);
    }

    public void deleteMoimByOwner(
            final WriterName writerName
    ) {
        moimRetriever.findByOwner(writerName).ifPresent(
                moim -> {
                    deleteRelatedData(moim);
                    deleteMoim(moim);
                }
        );
    }
}
