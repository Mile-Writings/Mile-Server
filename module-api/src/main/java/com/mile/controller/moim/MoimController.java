package com.mile.controller.moim;

import com.mile.common.auth.JwtTokenUpdater;
import com.mile.common.auth.annotation.UserAuthAnnotation;
import com.mile.common.auth.annotation.UserAuthenticationType;
import com.mile.common.auth.dto.AccessTokenDto;
import com.mile.common.resolver.moim.MoimIdPathVariable;
import com.mile.common.resolver.user.UserId;
import com.mile.common.utils.thread.WriterNameContextUtil;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.moim.service.MoimService;
import com.mile.moim.service.dto.MoimIdValueDto;
import com.mile.moim.service.dto.request.MoimCreateRequest;
import com.mile.moim.service.dto.request.MoimInfoModifyRequest;
import com.mile.moim.service.dto.request.TopicCreateRequest;
import com.mile.moim.service.dto.request.WriterMemberJoinRequest;
import com.mile.moim.service.dto.response.BestMoimListResponse;
import com.mile.moim.service.dto.response.ContentListResponse;
import com.mile.moim.service.dto.response.InvitationCodeGetResponse;
import com.mile.moim.service.dto.response.MoimAuthenticateResponse;
import com.mile.moim.service.dto.response.MoimCuriousPostListResponse;
import com.mile.moim.service.dto.response.MoimInfoOwnerResponse;
import com.mile.moim.service.dto.response.MoimInfoResponse;
import com.mile.moim.service.dto.response.MoimInvitationInfoResponse;
import com.mile.moim.service.dto.response.MoimMostCuriousWriterResponse;
import com.mile.moim.service.dto.response.MoimNameConflictCheckResponse;
import com.mile.moim.service.dto.response.MoimPublicStatusResponse;
import com.mile.moim.service.dto.response.MoimTopicInfoListResponse;
import com.mile.moim.service.dto.response.MoimTopicResponse;
import com.mile.moim.service.dto.response.MoimWriterNameListGetResponse;
import com.mile.moim.service.dto.response.TemporaryPostExistResponse;
import com.mile.moim.service.dto.response.TopicListResponse;
import com.mile.moim.service.dto.response.WriterNameConflictCheckResponse;
import com.mile.writername.domain.MoimRole;
import com.mile.writername.service.dto.response.WriterNameInformationResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/moim")
public class MoimController implements MoimControllerSwagger {

    private final MoimService moimService;
    private final JwtTokenUpdater jwtTokenUpdater;

    @Override
    @GetMapping("/{moimId}")
    @UserAuthAnnotation(UserAuthenticationType.WRITER_NAME)
    public SuccessResponse<ContentListResponse> getTopicsFromMoim(
            @MoimIdPathVariable final Long moimId,
            @UserId final Long userId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return SuccessResponse.of(SuccessMessage.TOPIC_SEARCH_SUCCESS,
                moimService.getContentsFromMoim(moimId));
    }


    @Override
    @GetMapping("/{moimId}/name/validation")
    public ResponseEntity<SuccessResponse<WriterNameConflictCheckResponse>> checkConflictOfWriterName(
            @MoimIdPathVariable final Long moimId,
            @RequestParam final String writerName,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.IS_CONFLICT_WRITER_NAME_GET_SUCCESS,
                moimService.checkConflictOfWriterName(moimId, writerName)));
    }

    @Override
    @PostMapping("/{moimId}/user")
    public ResponseEntity<SuccessResponse> joinMoim(
            @MoimIdPathVariable final Long moimId,
            @RequestBody @Valid final WriterMemberJoinRequest joinRequest,
            @UserId final Long userId,
            @PathVariable("moimId") final String moimUrl
    ) {
        final Long writerNameId = moimService.joinMoim(moimId, userId, joinRequest);
        return ResponseEntity
                .ok(SuccessResponse.of(SuccessMessage.WRITER_JOIN_SUCCESS,
                        AccessTokenDto.of(jwtTokenUpdater.setAccessToken(userId, moimId, writerNameId, MoimRole.WRITER)
                        )));
    }

    @Override
    @GetMapping("/{moimId}/invite")
    public ResponseEntity<SuccessResponse<MoimInvitationInfoResponse>> getInvitationInfo(
            @MoimIdPathVariable final Long moimId,
            @UserId final Long userId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_INVITE_INFO_GET_SUCCESS,
                moimService.getMoimInvitationInfo(userId, moimId)));
    }

    @Override
    @GetMapping("/{moimId}/authenticate")
    public SuccessResponse<MoimAuthenticateResponse> getAuthenticationOfMoim(
            @MoimIdPathVariable final Long moimId,
            @UserId final Long userId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_AUTHENTICATE_SUCCESS,
                moimService.getAuthenticateUserOfMoim(moimId, userId));
    }


    @Override
    @GetMapping("/{moimId}/writers/top-rank")
    public ResponseEntity<SuccessResponse<MoimMostCuriousWriterResponse>> getMostCuriousWritersOfMoim(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.of(SuccessMessage.MOIM_POPULAR_WRITER_SEARCH_SUCCESS,
                        moimService.getMostCuriousWritersOfMoim(moimId)));
    }

    @Override
    @GetMapping("/{moimId}/topic/today")
    public SuccessResponse<MoimTopicResponse> getTopicFromMoim(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_TOPIC_GET_SUCCESS, moimService.getTopicFromMoim(moimId));

    }

    @GetMapping("/{moimId}/info")
    @Override
    public SuccessResponse<MoimInfoResponse> getMoimInfo(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_INFO_SUCCESS, moimService.getMoimInfo(moimId));
    }


    @GetMapping("/{moimId}/topics")
    @Override
    public ResponseEntity<SuccessResponse<TopicListResponse>> getTopicList(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.of(SuccessMessage.TOPIC_LIST_SEARCH_SUCCESS, moimService.getTopicList(moimId)));
    }

    @Override
    @GetMapping("/{moimId}/posts/top-rank")
    public SuccessResponse<MoimCuriousPostListResponse> getMostCuriousPostByMoim(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_TOP_2_POST_GET_SUCCESS,
                moimService.getMostCuriousPostFromMoim(moimId));
    }

    @Override
    @GetMapping("/{moimId}/info/owner")
    @UserAuthAnnotation(UserAuthenticationType.OWNER)
    public ResponseEntity<SuccessResponse<MoimInfoOwnerResponse>> getMoimInfoForOwner(
            @MoimIdPathVariable final Long moimId,
            @UserId final Long userId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_INFO_FOR_OWNER_GET_SUCCESS, moimService.getMoimInfoForOwner(moimId)));
    }

    @Override
    @PostMapping("/{moimId}/topic")
    public ResponseEntity<SuccessResponse> createTopicOfMoim(
            @MoimIdPathVariable final Long moimId,
            @RequestBody @Valid final TopicCreateRequest createRequest,
            @UserId final Long userId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.created(URI.create(moimService.createTopic(moimId, userId, createRequest))).body(SuccessResponse.of(SuccessMessage.TOPIC_CREATE_SUCCESS));
    }

    @GetMapping("/best")
    public ResponseEntity<SuccessResponse<BestMoimListResponse>> getBestMoimAndPostList() {
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of(SuccessMessage.BEST_MOIM_POSTS_GET_SUCCESS,
                moimService.getBestMoimAndPostList()));
    }

    @Override
    @GetMapping("/{moimId}/temporary")
    public SuccessResponse<TemporaryPostExistResponse> getTemporaryPost(
            @MoimIdPathVariable final Long moimId,
            @UserId final Long userId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return SuccessResponse.of(SuccessMessage.IS_TEMPORARY_POST_EXIST_GET_SUCCESS,
                moimService.getTemporaryPost(moimId, userId));
    }


    @Override
    @GetMapping("/{moimId}/admin/topics")
    public ResponseEntity<SuccessResponse<MoimTopicInfoListResponse>> getMoimTopicList(
            @MoimIdPathVariable final Long moimId,
            @RequestParam final int page,
            @UserId final Long userId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_TOPIC_LIST_GET_SUCCESS, moimService.getMoimTopicList(moimId, userId, page)));
    }

    @Override
    @PutMapping("/{moimId}/info")
    public ResponseEntity<SuccessResponse> modifyMoimInformation(
            @MoimIdPathVariable final Long moimId,
            @RequestBody @Valid final MoimInfoModifyRequest request,
            @UserId final Long userId,
            @PathVariable("moimId") final String moimUrl
    ) {
        moimService.modifyMoimInforation(moimId, userId, request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_INFORMATION_PUT_SUCCESS));
    }

    @GetMapping("/name/validation")
    @Override
    public ResponseEntity<SuccessResponse<MoimNameConflictCheckResponse>> validateMoimName(
            @RequestParam final String moimName
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.IS_CONFLICT_MOIM_NAME_GET_SUCCESS,
                moimService.validateMoimName(moimName)));
    }

    @PostMapping
    @Override
    public ResponseEntity<SuccessResponse<AccessTokenDto>> createMoim(
            @RequestBody @Valid final MoimCreateRequest creatRequest,
            @UserId final Long userId
    ) {
        MoimIdValueDto moimIdValueDto = moimService.createMoim(userId, creatRequest);
        return ResponseEntity.ok(
                SuccessResponse.of(
                        SuccessMessage.MOIM_CREATE_SUCCESS,
                        AccessTokenDto.of(
                                moimIdValueDto.data(),
                                jwtTokenUpdater.setAccessToken(userId, moimIdValueDto.moimId(), moimIdValueDto.writerNameId(), MoimRole.OWNER))
                )
        );
    }

    @GetMapping("/{moimId}/invitation-code")
    @Override
    public ResponseEntity<SuccessResponse<InvitationCodeGetResponse>> getInvitationCode(
            @MoimIdPathVariable final Long moimId,
            @UserId final Long userId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.INVITATION_CODE_GET_SUCCESS, moimService.getInvitationCode(moimId, userId)));
    }

    @Override
    @GetMapping("/{moimId}/writernames")
    public ResponseEntity<SuccessResponse<MoimWriterNameListGetResponse>> getWriterNameListOfMoim(
            @MoimIdPathVariable final Long moimId,
            @RequestParam final int page,
            @UserId final Long userId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_WRITERNAME_LIST_GET_SUCCESS, moimService.getWriterNameListOfMoim(moimId, userId, page)));
    }

    @Override
    @UserAuthAnnotation(UserAuthenticationType.WRITER_NAME)
    @GetMapping("/{moimId}/writername")
    public ResponseEntity<SuccessResponse<WriterNameInformationResponse>> getWriterNameOfUser(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.WRITER_NAME_GET_SUCCESS, moimService.getWriterNameOfUser(WriterNameContextUtil.getWriterNameContext())));
    }

    @Override
    @GetMapping("/{moimId}/public-status")
    public SuccessResponse<MoimPublicStatusResponse> getPublicStatusOfMoim(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_PUBLIC_STATUS_GET_SUCCESS,
                moimService.getPublicStatusOfMoim(moimId));
    }

    @Override
    @DeleteMapping("/{moimId}")
    public ResponseEntity<SuccessResponse> deleteMoim(
            @MoimIdPathVariable final Long moimId,
            @UserId final Long userId,
            @PathVariable("moimId") final String moimUrl
    ) {
        moimService.deleteMoim(moimId, userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_DELETE_SUCCESS));
    }

    @Override
    @GetMapping("/{moimId}/information")
    public ResponseEntity<SuccessResponse> getMoimTotalInformation(
            @MoimIdPathVariable @Parameter(schema = @Schema(implementation = String.class), in = ParameterIn.PATH) final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_INFO_SUCCESS, moimService.getMoimTotalInformation(moimId)));
    }
}
