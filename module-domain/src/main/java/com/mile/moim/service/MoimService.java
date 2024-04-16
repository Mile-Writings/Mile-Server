package com.mile.moim.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.repository.MoimRepository;
import com.mile.moim.service.dto.BestMoimListResponse;
import com.mile.moim.service.dto.ContentListResponse;
import com.mile.moim.service.dto.InvitationCodeGetResponse;
import com.mile.moim.service.dto.MoimAuthenticateResponse;
import com.mile.moim.service.dto.MoimCreateRequest;
import com.mile.moim.service.dto.MoimCreateResponse;
import com.mile.moim.service.dto.MoimCuriousPostListResponse;
import com.mile.moim.service.dto.MoimInfoModifyRequest;
import com.mile.moim.service.dto.MoimInfoOwnerResponse;
import com.mile.moim.service.dto.MoimInfoResponse;
import com.mile.moim.service.dto.MoimInvitationInfoResponse;
import com.mile.moim.service.dto.MoimListOfUserResponse;
import com.mile.moim.service.dto.MoimNameConflictCheckResponse;
import com.mile.moim.service.dto.MoimOfUserResponse;
import com.mile.moim.service.dto.MoimTopicInfoListResponse;
import com.mile.moim.service.dto.MoimTopicResponse;
import com.mile.moim.service.dto.MoimWriterNameListGetResponse;
import com.mile.moim.service.dto.PopularWriterListResponse;
import com.mile.moim.service.dto.TemporaryPostExistResponse;
import com.mile.moim.service.dto.TopicCreateRequest;
import com.mile.moim.service.dto.TopicListResponse;
import com.mile.moim.service.dto.WriterMemberJoinRequest;
import com.mile.moim.service.dto.WriterNameConflictCheckResponse;
import com.mile.post.domain.Post;
import com.mile.post.service.PostAuthenticateService;
import com.mile.post.service.PostDeleteService;
import com.mile.post.service.PostGetService;
import com.mile.topic.service.TopicService;
import com.mile.user.domain.User;
import com.mile.user.service.UserService;
import com.mile.utils.DateUtil;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.WriterNameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MoimService {

    private final WriterNameService writerNameService;
    private final TopicService topicService;
    private final UserService userService;
    private final MoimRepository moimRepository;
    private final PostDeleteService postCuriousService;
    private final PostAuthenticateService postAuthenticateService;
    private final PostGetService postGetService;
    private final SecureUrlUtil secureUrlUtil;
    private static final int WRITER_NAME_MAX_VALUE = 8;
    private static final int MOIM_NAME_MAX_VALUE = 10;

    public ContentListResponse getContentsFromMoim(
            final Long moimId,
            final Long userId
    ) {
        postAuthenticateService.authenticateUserOfMoim(moimId, userId);
        return ContentListResponse.of(topicService.getContentsFromMoim(moimId));
    }

    public WriterNameConflictCheckResponse checkConflictOfWriterName(Long moimId, String writerName) {
        if (writerName.length() > WRITER_NAME_MAX_VALUE) {
            throw new BadRequestException(ErrorMessage.WRITER_NAME_LENGTH_WRONG);
        }
        return WriterNameConflictCheckResponse.of(writerNameService.existWriterNamesByMoimAndName(findById(moimId), writerName));
    }

    public Long joinMoim(
            final Long moimId,
            final Long userId,
            final WriterMemberJoinRequest joinRequest
    ) {
        return writerNameService.createWriterName(userService.findById(userId), findById(moimId), joinRequest);
    }

    public MoimInvitationInfoResponse getMoimInvitationInfo(
            final Long userId,
            final Long moimId
    ) {
        isUserAlreadyInMoim(moimId, userId);
        return MoimInvitationInfoResponse.of(findById(moimId), writerNameService.findNumbersOfWritersByMoimId(moimId));
    }

    private void isUserAlreadyInMoim(
            final Long moimId,
            final Long userId
    ) {
        if (writerNameService.findMemberByMoimIdANdWriterId(moimId, userId).isPresent()) {
            throw new BadRequestException(ErrorMessage.USER_MOIM_ALREADY_JOIN);
        }
    }

    public void authenticateOwnerOfMoim(
            final Moim moim,
            final Long userId
    ) {
        if (!isMoimOwnerEqualsUser(moim, userService.findById(userId))) {
            throw new ForbiddenException(ErrorMessage.MOIM_OWNER_AUTHENTICATION_ERROR);
        }
    }

    private boolean isMoimOwnerEqualsUser(
            final Moim moim,
            final User user
    ) {
        return moim.getOwner().getWriter().equals(user);
    }

    public MoimAuthenticateResponse getAuthenticateUserOfMoim(
            final Long moimId,
            final Long userId
    ) {
        return MoimAuthenticateResponse.of(writerNameService.isUserInMoim(moimId, userId), isMoimOwnerEqualsUser(findById(moimId), userService.findById(userId)));
    }

    public Moim findById(
            final Long moimId
    ) {
        return moimRepository.findById(moimId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MOIM_NOT_FOUND)
        );
    }

    public PopularWriterListResponse getMostCuriousWritersOfMoim(
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
        List<Moim> moims = moimRepository.findTop3MoimsByPostCountInLastWeek();
        return moims;
    }

    public BestMoimListResponse getBestMoimAndPostList() {

        List<Moim> bestMoimsByPostNumber = getBestMoimByPostNumber();

        Map<Moim, List<Post>> bestMoimAndPostMap = bestMoimsByPostNumber.stream()
                .collect(Collectors.toMap(
                        moim -> moim,
                        postGetService::getLatestPostsByMoim
                ));

        return BestMoimListResponse.of(bestMoimAndPostMap);
    }

    public TemporaryPostExistResponse getTemporaryPost(
            final Long moimId,
            final Long userId
    ) {
        String postId = postGetService.getTemporaryPostExist(findById(moimId), writerNameService.findByWriterId(userId));
        return TemporaryPostExistResponse.of(!secureUrlUtil.decodeUrl(postId).equals(0L), postId);
    }

    public MoimTopicInfoListResponse getMoimTopicList(
            final Long moimId,
            final Long userId,
            final int page
    ) {
        getAuthenticateOwnerOfMoim(moimId, userId);
        return topicService.getTopicListFromMoim(moimId, page);
    }

    private void getAuthenticateOwnerOfMoim(
            final Long moimId,
            final Long userId
    ) {
        Long writerNameId = writerNameService.getWriterNameIdByMoimIdAndUserId(moimId, userId);
        Moim moim = findById(moimId);
        if (!moim.getOwner().getId().equals(writerNameId)) {
            throw new ForbiddenException(ErrorMessage.OWNER_AUTHENTICATE_ERROR);
        }
    }

    @Transactional
    public void modifyMoimInforation(
            final Long moimId,
            final Long userId,
            final MoimInfoModifyRequest modifyRequest
    ) {
        Moim moim = findById(moimId);
        moim.modifyMoimInfo(modifyRequest);
        authenticateOwnerOfMoim(moim, userId);
    }

    public MoimNameConflictCheckResponse validateMoimName(
            final String moimName
    ) {
        if (moimName.length() > MOIM_NAME_MAX_VALUE) {
            throw new BadRequestException(ErrorMessage.MOIM_NAME_LENGTH_WRONG);
        }
        return MoimNameConflictCheckResponse.of(!moimRepository.existsByName(moimName));
    }

    public InvitationCodeGetResponse getInvitationCode(
            final Long moimId,
            final Long userId
    ) {
        Moim moim = findById(moimId);
        authenticateOwnerOfMoim(moim, userId);
        return InvitationCodeGetResponse.of(moim.getIdUrl());
    }

    public String createTopic(
            final Long moimId,
            final Long userId,
            final TopicCreateRequest createRequest
    ) {
        Moim moim = findById(moimId);
        authenticateOwnerOfMoim(moim, userId);
        return topicService.createTopicOfMoim(moim, createRequest).toString();
    }

    @Transactional
    public MoimCreateResponse createMoim(
            final Long userId,
            final MoimCreateRequest createRequest
    ) {
        Moim moim = moimRepository.saveAndFlush(Moim.create(createRequest));
        User user = userService.findById(userId);

        setMoimOwner(moim, user, createRequest);
        setFirstTopic(moim, userId, createRequest);

        return MoimCreateResponse.of(moim.getIdUrl(), moim.getIdUrl());
    }

    private void setMoimOwner(
            final Moim moim,
            final User user,
            final MoimCreateRequest createRequest
    ) {
        WriterMemberJoinRequest joinRequest = WriterMemberJoinRequest.of(createRequest.writerName(), createRequest.writerNameDescription());
        WriterName owner = writerNameService.getById(writerNameService.createWriterName(user, moim, joinRequest));
        moim.setOwner(owner);
        moim.setIdUrl(secureUrlUtil.encodeUrl(moim.getId()));
    }


    private void setFirstTopic(
            final Moim moim,
            final Long userId,
            final MoimCreateRequest createRequest
    ) {
        TopicCreateRequest topicRequest = TopicCreateRequest.of(createRequest.topic(), createRequest.topicTag(),
                createRequest.topicDescription());
        createTopic(moim.getId(), userId, topicRequest);
    }

    public MoimInfoOwnerResponse getMoimInfoForOwner(
            final Long moimId,
            final Long userId
    ) {
        Moim moim = findById(moimId);
        authenticateOwnerOfMoim(moim, userId);
        return MoimInfoOwnerResponse.of(moim);
    }

    public MoimWriterNameListGetResponse getWriterNameListOfMoim(
            final Long moimId,
            final Long userId,
            final int page
    ) {
        Moim moim = findById(moimId);
        authenticateOwnerOfMoim(moim, userId);
        return writerNameService.getWriterNameInfoList(moimId, page);
    }

    public MoimListOfUserResponse getMoimOfUserList(
            final Long userId
    ) {
        return MoimListOfUserResponse.of(writerNameService.getMoimListOfUser(userId)
                .stream()
                .map(moim -> MoimOfUserResponse.of(moim))
                .collect(Collectors.toList()));
    }
}
