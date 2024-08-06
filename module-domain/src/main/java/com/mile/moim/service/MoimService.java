package com.mile.moim.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ForbiddenException;
import com.mile.moim.domain.Moim;
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
import com.mile.moim.service.dto.MoimNameConflictCheckResponse;
import com.mile.moim.service.dto.MoimPublicStatusResponse;
import com.mile.moim.service.dto.MoimTopicInfoListResponse;
import com.mile.moim.service.dto.MoimTopicResponse;
import com.mile.moim.service.dto.MoimWriterNameListGetResponse;
import com.mile.moim.service.dto.PopularWriterListResponse;
import com.mile.moim.service.dto.TemporaryPostExistResponse;
import com.mile.moim.service.dto.TopicCreateRequest;
import com.mile.moim.service.dto.TopicListResponse;
import com.mile.moim.service.dto.WriterMemberJoinRequest;
import com.mile.moim.service.dto.WriterNameConflictCheckResponse;
import com.mile.moim.service.lock.AtomicValidateUniqueMoimName;
import com.mile.post.domain.Post;
import com.mile.post.service.PostRetriever;
import com.mile.topic.service.TopicCreator;
import com.mile.topic.service.TopicRemover;
import com.mile.topic.service.TopicRetriever;
import com.mile.user.domain.User;
import com.mile.user.service.UserRetriever;
import com.mile.utils.DateUtil;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.WriterNameRemover;
import com.mile.writername.service.WriterNameRetriever;
import com.mile.writername.service.WriterNameService;
import com.mile.writername.service.dto.WriterNameShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoimService {

    private final WriterNameService writerNameService;
    private final WriterNameRetriever writerNameRetriever;
    private final UserRetriever userRetriever;
    private final PostRetriever postRetriever;
    private final SecureUrlUtil secureUrlUtil;
    private final MoimRemover moimRemover;
    private final MoimRetriever moimRetriever;
    private final MoimCreator moimCreator;
    private final WriterNameRemover writerNameRemover;

    private final TopicRemover topicRemover;
    private final TopicRetriever topicRetriever;
    private final TopicCreator topicCreator;

    private static final int WRITER_NAME_MAX_VALUE = 8;
    private static final int MOIM_NAME_MAX_VALUE = 10;
    private static final int BEST_MOIM_DEFAULT_NUMBER = 3;

    public ContentListResponse getContentsFromMoim(
            final Long moimId,
            final Long userId
    ) {
        postRetriever.authenticateUserOfMoim(writerNameRetriever.isUserInMoim(moimId, userId));
        return ContentListResponse.of(topicRetriever.getContentsFromMoim(moimId));
    }

    public WriterNameShortResponse getWriterNameOfUser(
            final Long moimId,
            final Long userId
    ) {
        return writerNameRetriever.findWriterNameInfo(moimId, userId);
    }

    public WriterNameConflictCheckResponse checkConflictOfWriterName(Long moimId, String writerName) {
        if (writerName.length() > WRITER_NAME_MAX_VALUE) {
            throw new BadRequestException(ErrorMessage.WRITER_NAME_LENGTH_WRONG);
        }
        String normalizedWriterName = writerName.replaceAll("\\s+", "").toLowerCase();
        return WriterNameConflictCheckResponse.of(writerNameRetriever.existWriterNamesByMoimAndName(moimRetriever.findById(moimId), normalizedWriterName));
    }

    public Long joinMoim(
            final Long moimId,
            final Long userId,
            final WriterMemberJoinRequest joinRequest
    ) {
        return writerNameService.createWriterName(userRetriever.findById(userId), moimRetriever.findById(moimId), joinRequest);
    }

    public MoimInvitationInfoResponse getMoimInvitationInfo(
            final Long userId,
            final Long moimId
    ) {
        isUserAlreadyInMoim(moimId, userId);
        return MoimInvitationInfoResponse.of(moimRetriever.findById(moimId), writerNameRetriever.findNumbersOfWritersByMoimId(moimId));
    }

    private void isUserAlreadyInMoim(
            final Long moimId,
            final Long userId
    ) {
        if (writerNameRetriever.findMemberByMoimIdAndWriterId(moimId, userId).isPresent()) {
            throw new BadRequestException(ErrorMessage.USER_MOIM_ALREADY_JOIN);
        }
    }

    public MoimAuthenticateResponse getAuthenticateUserOfMoim(
            final Long moimId,
            final Long userId
    ) {
        return MoimAuthenticateResponse.of(writerNameRetriever.isUserInMoim(moimId, userId), moimRetriever.isMoimOwnerEqualsUser(moimRetriever.findById(moimId), userId));
    }

    public PopularWriterListResponse getMostCuriousWritersOfMoim(
            final Long moimId
    ) {
        List<WriterName> writers = writerNameRetriever.findTop2ByCuriousCount(moimId);
        return PopularWriterListResponse.of(writers);
    }


    public MoimTopicResponse getTopicFromMoim(
            final Long moimId
    ) {
        return MoimTopicResponse.of(topicRetriever.findLatestTopicByMoim(moimRetriever.findById(moimId)));
    }

    public MoimInfoResponse getMoimInfo(
            final Long moimId
    ) {
        Moim moim = moimRetriever.findById(moimId);
        return MoimInfoResponse.of(
                moim.getImageUrl(),
                moim.getName(),
                moim.getOwner().getName(),
                moim.getInformation(),
                writerNameRetriever.findNumbersOfWritersByMoimId(moimId),
                DateUtil.getStringDateOfLocalDate(moim.getCreatedAt())
        );
    }

    public MoimCuriousPostListResponse getMostCuriousPostFromMoim(final Long moimId) {
        return postRetriever.getMostCuriousPostByMoim(moimRetriever.findById(moimId));
    }

    public TopicListResponse getTopicList(
            final Long moimId
    ) {
        return TopicListResponse.of(topicRetriever.getKeywordsFromMoim(moimId));
    }

    public void getAuthenticateOwnerOfMoim(
            final Long moimId,
            final Long userId
    ) {
        Long writerNameId = writerNameRetriever.getWriterNameIdByMoimIdAndUserId(moimId, userId);
        Moim moim = moimRetriever.findById(moimId);
        if (!moim.getOwner().getId().equals(writerNameId)) {
            throw new ForbiddenException(ErrorMessage.MOIM_OWNER_AUTHENTICATION_ERROR);
        }
    }


    public List<Moim> getBestMoimByPostNumber() {

        List<Moim> moims = moimRetriever.findBestMoims();

        if (moims.size() < BEST_MOIM_DEFAULT_NUMBER) {
            int remaining = BEST_MOIM_DEFAULT_NUMBER - moims.size();
            List<Moim> latestMoims = moimRetriever.getLatestMoims(remaining, moims);
            moims.addAll(latestMoims);
        }

        return moims;
    }

    public BestMoimListResponse getBestMoimAndPostList() {

        List<Moim> bestMoimsByPostNumber = getBestMoimByPostNumber();

        Map<Moim, List<Post>> bestMoimAndPostMap = bestMoimsByPostNumber.stream()
                .collect(Collectors.toMap(
                        moim -> moim,
                        postRetriever::getLatestPostsByMoim
                ));

        return BestMoimListResponse.of(bestMoimAndPostMap);
    }

    public TemporaryPostExistResponse getTemporaryPost(
            final Long moimId,
            final Long userId
    ) {
        String postId = postRetriever.getTemporaryPostExist(moimRetriever.findById(moimId), writerNameRetriever.findByMoimAndUser(moimId, userId));
        return TemporaryPostExistResponse.of(!secureUrlUtil.decodeUrl(postId).equals(0L), postId);
    }

    public MoimTopicInfoListResponse getMoimTopicList(
            final Long moimId,
            final Long userId,
            final int page
    ) {
        getAuthenticateOwnerOfMoim(moimId, userId);
        return topicRetriever.getTopicListFromMoim(moimId, page);
    }


    @AtomicValidateUniqueMoimName
    public void modifyMoimInforation(
            final Long moimId,
            final Long userId,
            final MoimInfoModifyRequest modifyRequest
    ) {
        checkMoimNameUnique(modifyRequest.moimTitle());
        Moim moim = moimRetriever.findById(moimId);
        moimRetriever.authenticateOwnerOfMoim(moim, userRetriever.findById(userId));
        moim.modifyMoimInfo(modifyRequest);
    }

    @AtomicValidateUniqueMoimName
    public MoimNameConflictCheckResponse validateMoimName(
            final String moimName
    ) {
        String normalizedMoimName = moimName.replaceAll("\\s+", "").toLowerCase();
        if (moimName.length() > MOIM_NAME_MAX_VALUE) {
            throw new BadRequestException(ErrorMessage.MOIM_NAME_VALIDATE_ERROR);
        }
        return MoimNameConflictCheckResponse.of(moimRetriever.validateNormalizedName(normalizedMoimName));
    }


    @AtomicValidateUniqueMoimName
    public void checkMoimNameUnique(
            final String moimName
    ) {
        if (!moimRetriever.validateNormalizedName(moimName)) {
            throw new BadRequestException(ErrorMessage.MOIM_NAME_VALIDATE_ERROR);
        }
    }

    public InvitationCodeGetResponse getInvitationCode(
            final Long moimId,
            final Long userId
    ) {
        Moim moim = moimRetriever.findById(moimId);
        moimRetriever.authenticateOwnerOfMoim(moim, userRetriever.findById(userId));
        return InvitationCodeGetResponse.of(moim.getIdUrl());
    }

    public String createTopic(
            final Long moimId,
            final Long userId,
            final TopicCreateRequest createRequest
    ) {
        Moim moim = moimRetriever.findById(moimId);
        moimRetriever.authenticateOwnerOfMoim(moim, userRetriever.findById(userId));
        return topicCreator.createTopicOfMoim(moim, createRequest).toString();
    }

    @AtomicValidateUniqueMoimName
    public MoimCreateResponse createMoim(
            final Long userId,
            final MoimCreateRequest createRequest
    ) {
        checkMoimNameUnique(createRequest.moimName());
        Moim moim = moimCreator.createMoim(createRequest);
        User user = userRetriever.findById(userId);

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
        WriterName owner = writerNameRetriever.findById(writerNameService.createWriterName(user, moim, joinRequest));
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
        Moim moim = moimRetriever.findById(moimId);
        moimRetriever.authenticateOwnerOfMoim(moim, userRetriever.findById(userId));
        return MoimInfoOwnerResponse.of(moim);
    }

    public MoimWriterNameListGetResponse getWriterNameListOfMoim(
            final Long moimId,
            final Long userId,
            final int page
    ) {
        Moim moim = moimRetriever.findById(moimId);
        moimRetriever.authenticateOwnerOfMoim(moim, userRetriever.findById(userId));
        return writerNameService.getWriterNameInfoList(moim, page);
    }

    public MoimPublicStatusResponse getPublicStatusOfMoim(
            final Long moimId
    ) {
        return MoimPublicStatusResponse.of(moimRetriever.findById(moimId).isPublic());
    }

    public void deleteMoim(
            final Long moimId,
            final Long userId
    ) {

        Moim moim = moimRetriever.findById(moimId);
        moimRetriever.authenticateOwnerOfMoim(moim, userRetriever.findById(userId));
        moimRemover.deleteRelatedData(moim);
        writerNameRemover.deleteWriterNamesByMoim(moim);
        topicRemover.deleteTopicsByMoim(moim);
        writerNameRemover.setWriterNameMoimNull(moim.getOwner());
        moimRemover.deleteMoim(moim);
    }
}
