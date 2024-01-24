package com.mile.moim.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.repository.MoimRepository;
import com.mile.moim.service.dto.BestMoimListResponse;
import com.mile.moim.service.dto.ContentListResponse;
import com.mile.moim.service.dto.MoimAuthenticateResponse;
import com.mile.moim.service.dto.MoimCuriousPostListResponse;
import com.mile.moim.service.dto.MoimInfoResponse;
import com.mile.moim.service.dto.MoimTopicResponse;
import com.mile.moim.service.dto.TemporaryPostExistResponse;
import com.mile.moim.service.dto.TopicListResponse;
import com.mile.post.domain.Post;
import com.mile.post.service.PostAuthenticateService;
import com.mile.post.service.PostDeleteService;
import com.mile.post.service.PostGetService;
import com.mile.post.service.PostCreateService;
import com.mile.topic.service.TopicService;
import com.mile.utils.DateUtil;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.WriterNameService;
import com.mile.writername.service.dto.PopularWriterListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MoimService {

    private final WriterNameService writerNameService;
    private final TopicService topicService;
    private final MoimRepository moimRepository;
    private final PostDeleteService postCuriousService;
    private final PostAuthenticateService postAuthenticateService;
    private final PostCreateService postCreateService;
    private final PostGetService postGetService;
    private final SecureUrlUtil secureUrlUtil;

    public ContentListResponse getContentsFromMoim(
            final Long moimId,
            final Long userId
    ) {
        postAuthenticateService.authenticateUserOfMoim(moimId, userId);
        return ContentListResponse.of(topicService.getContentsFromMoim(moimId));
    }


    public void authenticateUserOfMoim(
            final Long moimId,
            final Long userId
    ) {
        if (!writerNameService.isUserInMoim(moimId, userId)) {
            throw new ForbiddenException(ErrorMessage.USER_AUTHENTICATE_ERROR);
        }
    }

    public MoimAuthenticateResponse getAuthenticateUserOfMoim(
            final Long moimId,
            final Long userId
    ) {
        return MoimAuthenticateResponse.of(writerNameService.isUserInMoim(moimId, userId));
    }

    public Moim findById(
            final Long moimId
    ) {
        return moimRepository.findById(moimId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MOIM_NOT_FOUND)
        );
    }

    public PopularWriterListResponse getMostCuriousWriters(
            final Long moimId
    ) {
        List<WriterName> writers = writerNameService.findTop2ByCuriousCount(moimId);
        return PopularWriterListResponse.of(writers);
    }


    public MoimTopicResponse getTopicFromMoim(
            final Long moimId
    ) {
        return MoimTopicResponse.of(topicService.findLatestTopicByMoim(findById(moimId)));
    }

    public MoimInfoResponse getMoimInfo(
            final Long moimId
    ) {
        Moim moim = findById(moimId);
        return MoimInfoResponse.of(
                moim.getImageUrl(),
                moim.getName(),
                moim.getOwner().getName(),
                moim.getInformation(),
                writerNameService.findNumbersOfWritersByMoimId(moimId),
                DateUtil.getStringDateOfLocalDate(moim.getCreatedAt())
        );
    }

    public MoimCuriousPostListResponse getMostCuriousPostFromMoim(final Long moimId) {
        return postCuriousService.getMostCuriousPostByMoim(findById(moimId));
    }

    public TopicListResponse getTopicList(
            final Long moimId
    ) {
        return TopicListResponse.of(topicService.getKeywordsFromMoim(moimId));
    }

    public List<Moim> getBestMoimByPostNumber() {
        LocalDateTime beforeWeek = LocalDateTime.now().minusWeeks(1);
        List<Moim> moims = moimRepository.findTop3MoimsByPostCount();
        return moims;
    }

    public BestMoimListResponse getBestMoimAndPostList() {
        Map<Moim, List<Post>> BestMoimAndPost = new HashMap<>();
        List<Moim> bestMoimsByPostNumber = getBestMoimByPostNumber();
        for (Moim moim : bestMoimsByPostNumber) {
            List<Post> latestPosts = postGetService.getLatestPostsByMoim(moim);
            BestMoimAndPost.put(moim, latestPosts);
        }
        return BestMoimListResponse.of(BestMoimAndPost);
    }

    public TemporaryPostExistResponse getTemporaryPost(
            final Long moimId,
            final Long userId
    ) {
        String postId = postCreateService.getTemporaryPostExist(findById(moimId), writerNameService.findByWriterId(userId));
        return TemporaryPostExistResponse.of(!secureUrlUtil.decodeUrl(postId).equals(0L), postId);
    }
}
