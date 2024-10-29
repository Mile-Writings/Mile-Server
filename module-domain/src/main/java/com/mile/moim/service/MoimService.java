package com.mile.moim.service;

import com.mile.common.utils.DateUtil;
import com.mile.common.utils.SecureUrlUtil;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.BadRequestException;
import com.mile.exception.model.ForbiddenException;
import com.mile.moim.domain.Moim;
import com.mile.moim.domain.popular.MoimCuriousWriter;
import com.mile.moim.domain.popular.MoimPopularInfo;
import com.mile.moim.service.dto.MoimIdValueDto;
import com.mile.moim.service.dto.request.MoimCreateRequest;
import com.mile.moim.service.dto.request.MoimInfoModifyRequest;
import com.mile.moim.service.dto.request.TopicCreateRequest;
import com.mile.moim.service.dto.request.WriterMemberJoinRequest;
import com.mile.moim.service.dto.response.BestMoimListResponse;
import com.mile.moim.service.dto.response.ContentListResponse;
import com.mile.moim.service.dto.response.InvitationCodeGetResponse;
import com.mile.moim.service.dto.response.MoimAuthenticateResponse;
import com.mile.moim.service.dto.response.MoimCreateResponse;
import com.mile.moim.service.dto.response.MoimCuriousPostListResponse;
import com.mile.moim.service.dto.response.MoimInfoOwnerResponse;
import com.mile.moim.service.dto.response.MoimInfoResponse;
import com.mile.moim.service.dto.response.MoimInvitationInfoResponse;
import com.mile.moim.service.dto.response.MoimMostCuriousPostResponse;
import com.mile.moim.service.dto.response.MoimMostCuriousWriterResponse;
import com.mile.moim.service.dto.response.MoimNameConflictCheckResponse;
import com.mile.moim.service.dto.response.MoimOverallInfoResponse;
import com.mile.moim.service.dto.response.MoimPublicStatusResponse;
import com.mile.moim.service.dto.response.MoimTopicInfoListResponse;
import com.mile.moim.service.dto.response.MoimTopicResponse;
import com.mile.moim.service.dto.response.MoimWriterNameListGetResponse;
import com.mile.moim.service.dto.response.TemporaryPostExistResponse;
import com.mile.moim.service.dto.response.TopicListResponse;
import com.mile.moim.service.dto.response.WriterNameConflictCheckResponse;
import com.mile.moim.service.lock.AtomicValidateUniqueMoimName;
import com.mile.moim.service.popular.MoimPopularInfoService;
import com.mile.post.domain.Post;
import com.mile.post.service.PostRetriever;
import com.mile.topic.service.TopicCreator;
import com.mile.topic.service.TopicRemover;
import com.mile.topic.service.TopicRetriever;
import com.mile.user.domain.User;
import com.mile.user.service.UserRetriever;
import com.mile.writername.domain.MoimRole;
import com.mile.writername.domain.WriterName;
import com.mile.writername.service.WriterNameRemover;
import com.mile.writername.service.WriterNameRetriever;
import com.mile.writername.service.WriterNameService;
import com.mile.writername.service.dto.response.WriterNameInformationResponse;
import com.mile.writername.service.vo.WriterNameInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final MoimPopularInfoService moimPopularInfoService;

    private static final int WRITER_NAME_MAX_VALUE = 8;
    private static final int MOIM_NAME_MAX_VALUE = 10;
    private static final int BEST_MOIM_DEFAULT_NUMBER = 3;

    public ContentListResponse getContentsFromMoim(
            final Long moimId
    ) {
        return ContentListResponse.of(topicRetriever.getContentsFromMoim(moimId));
    }

    public WriterNameInformationResponse getWriterNameOfUser(
            final Long writerNameId
    ) {
        return writerNameRetriever.findWriterNameInfo(writerNameId);
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
            final Long moimId,
            final HashMap<Long, WriterNameInfo> writerNameInfoHashMap
    ) {
        isUserAlreadyInMoim(moimId, writerNameInfoHashMap);
        return MoimInvitationInfoResponse.of(moimRetriever.findById(moimId), writerNameRetriever.findNumbersOfWritersByMoimId(moimId));
    }

    private void isUserAlreadyInMoim(
            final Long moimId,
            final HashMap<Long, WriterNameInfo> writerNameInfoHashMap
    ) {
        if (writerNameInfoHashMap.containsKey(moimId)) {
            throw new BadRequestException(ErrorMessage.USER_MOIM_ALREADY_JOIN);
        }
    }

    public MoimAuthenticateResponse getAuthenticateUserOfMoim(
            final Long moimId,
            final HashMap<Long, WriterNameInfo> writerNameMap
    ) {

        final boolean isMember = writerNameMap.containsKey(moimId);
        return MoimAuthenticateResponse.of(
                //멤버인지
                isMember,
                //Owner인지
                isMember && writerNameMap.get(moimId).moimRole().equals(MoimRole.OWNER)
        );
    }

    public MoimMostCuriousWriterResponse getMostCuriousWritersOfMoim(
            final Long moimId
    ) {
        Moim moim = moimRetriever.findById(moimId);
        List<WriterName> writers = writerNameRetriever.findTop2ByCuriousCount(moim);
        Set<MoimCuriousWriter> curiousWriters = writers.stream().map(MoimCuriousWriter::of).collect(Collectors.toSet());
        return MoimMostCuriousWriterResponse.of(curiousWriters);
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


    public MoimOverallInfoResponse getMoimTotalInformation(final Long moimId) {
        Moim moim = moimRetriever.findById(moimId);
        MoimInfoResponse moimInfoResponse = moimRetriever.getMoimInfoForTotal(moim, writerNameRetriever.findNumbersOfWritersByMoim(moim));
        MoimPopularInfo moimPopularInfo = moimPopularInfoService.getMoimPopularInfo(moim);
        MoimMostCuriousWriterResponse mostCuriousWriterResponse = MoimMostCuriousWriterResponse.of(moimPopularInfo.getWriters());
        MoimCuriousPostListResponse moimCuriousPostListResponse = MoimCuriousPostListResponse.of(
                moimPopularInfo.getPosts().stream().map(MoimMostCuriousPostResponse::of).toList()
        );
        return new MoimOverallInfoResponse(moimInfoResponse, moimCuriousPostListResponse, mostCuriousWriterResponse);
    }

    public MoimCuriousPostListResponse getMostCuriousPostFromMoim(final Long moimId) {
        Moim moim = moimRetriever.findById(moimId);
        return postRetriever.getMostCuriousPostByMoim(moim);
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
            final Long writerNameId
    ) {
        String postId = postRetriever.getTemporaryPostExist(moimRetriever.findById(moimId), writerNameId);
        return TemporaryPostExistResponse.of(!secureUrlUtil.decodeUrl(postId).equals(0L), postId);
    }

    public MoimTopicInfoListResponse getMoimTopicList(
            final Long moimId,
            final int page
    ) {
        return topicRetriever.getTopicListFromMoim(moimId, page);
    }


    @AtomicValidateUniqueMoimName
    public void modifyMoimInforation(
            final Long moimId,
            final MoimInfoModifyRequest modifyRequest
    ) {
        Moim moim = moimRetriever.findById(moimId);

        if (!moim.getName().equals(modifyRequest.moimTitle())) {
            validateMoimName(modifyRequest.moimTitle());
        }

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
            final Long moimId
    ) {
        Moim moim = moimRetriever.findById(moimId);
        return InvitationCodeGetResponse.of(moim.getIdUrl());
    }

    public String createTopic(
            final Long moimId,
            final TopicCreateRequest createRequest
    ) {
        Moim moim = moimRetriever.findById(moimId);
        return topicCreator.createTopicOfMoim(moim, createRequest).toString();
    }

    @AtomicValidateUniqueMoimName
    public MoimIdValueDto createMoim(
            final Long userId,
            final MoimCreateRequest createRequest
    ) {
        checkMoimNameUnique(createRequest.moimName());
        Moim moim = moimCreator.createMoim(createRequest);
        User user = userRetriever.findById(userId);

        final Long writerNameId = setMoimOwner(moim, user, createRequest);
        setFirstTopic(moim, createRequest);

        return MoimIdValueDto.of(moim.getId(), writerNameId, MoimCreateResponse.of(moim.getIdUrl(), moim.getIdUrl()));
    }

    private Long setMoimOwner(
            final Moim moim,
            final User user,
            final MoimCreateRequest createRequest
    ) {
        WriterMemberJoinRequest joinRequest = WriterMemberJoinRequest.of(createRequest.writerName(), createRequest.writerNameDescription());
        WriterName owner = writerNameRetriever.findById(writerNameService.createWriterName(user, moim, joinRequest));
        moim.setOwner(owner);
        moim.setIdUrl(secureUrlUtil.encodeUrl(moim.getId()));
        return owner.getId();
    }


    private void setFirstTopic(
            final Moim moim,
            final MoimCreateRequest createRequest
    ) {
        TopicCreateRequest topicRequest = TopicCreateRequest.of(createRequest.topic(), createRequest.topicTag(),
                createRequest.topicDescription());
        createTopic(moim.getId(), topicRequest);
    }

    public MoimInfoOwnerResponse getMoimInfoForOwner(
            final Long moimId
    ) {
        Moim moim = moimRetriever.findById(moimId);
        return MoimInfoOwnerResponse.of(moim);
    }

    public MoimWriterNameListGetResponse getWriterNameListOfMoim(
            final Long moimId,
            final int page
    ) {
        Moim moim = moimRetriever.findById(moimId);
        return writerNameService.getWriterNameInfoList(moim, page);
    }

    public MoimPublicStatusResponse getPublicStatusOfMoim(
            final Long moimId
    ) {
        return MoimPublicStatusResponse.of(moimRetriever.findById(moimId).isPublic());
    }

    public void deleteMoim(
            final Long moimId
    ) {

        Moim moim = moimRetriever.findById(moimId);
        moimRemover.deleteRelatedData(moim);
        writerNameRemover.deleteWriterNamesByMoim(moim);
        topicRemover.deleteTopicsByMoim(moim);
        writerNameRemover.setWriterNameMoimNull(moim.getOwner());
        moimRemover.deleteMoim(moim);
    }
}
