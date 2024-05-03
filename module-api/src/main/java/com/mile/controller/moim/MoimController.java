package com.mile.controller.moim;

import com.mile.authentication.PrincipalHandler;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.moim.service.MoimService;
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
import com.mile.moim.service.dto.MoimTopicInfoListResponse;
import com.mile.moim.service.dto.MoimTopicResponse;
import com.mile.moim.service.dto.MoimWriterNameListGetResponse;
import com.mile.moim.service.dto.PopularWriterListResponse;
import com.mile.moim.service.dto.TemporaryPostExistResponse;
import com.mile.moim.service.dto.TopicCreateRequest;
import com.mile.moim.service.dto.TopicListResponse;
import com.mile.moim.service.dto.WriterMemberJoinRequest;
import com.mile.moim.service.dto.WriterNameConflictCheckResponse;
import com.mile.resolver.moim.MoimIdPathVariable;
import com.mile.writername.service.dto.WriterNameShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final PrincipalHandler principalHandler;

    @Override
    @GetMapping("/{moimId}")
    public SuccessResponse<ContentListResponse> getTopicsFromMoim(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return SuccessResponse.of(SuccessMessage.TOPIC_SEARCH_SUCCESS,
                moimService.getContentsFromMoim(moimId, principalHandler.getUserIdFromPrincipal()));
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
    @PostMapping("{moimId}/user")
    public ResponseEntity<SuccessResponse> joinMoim(
            @MoimIdPathVariable final Long moimId,
            @RequestBody final WriterMemberJoinRequest joinRequest,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.created(URI.create(
                        moimService.joinMoim(moimId, principalHandler.getUserIdFromPrincipal(), joinRequest).toString()))
                .body(SuccessResponse.of(SuccessMessage.WRITER_JOIN_SUCCESS));
    }

    @Override
    @GetMapping("/{moimId}/invite")
    public ResponseEntity<SuccessResponse<MoimInvitationInfoResponse>> getInvitationInfo(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_INVITE_INFO_GET_SUCCESS,
                moimService.getMoimInvitationInfo(principalHandler.getUserIdFromPrincipal(), moimId)));
    }

    @Override
    @GetMapping("/{moimId}/authenticate")
    public SuccessResponse<MoimAuthenticateResponse> getAuthenticationOfMoim(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_AUTHENTICATE_SUCCESS,
                moimService.getAuthenticateUserOfMoim(moimId, principalHandler.getUserIdFromPrincipal()));
    }

    @Override
    @GetMapping("/{moimId}/writers/top-rank")
    public ResponseEntity<SuccessResponse<PopularWriterListResponse>> getMostCuriousWritersOfMoim(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.of(SuccessMessage.MOIM_POPULAR_WRITER_SEARCH_SUCCESS,
                        moimService.getMostCuriousWritersOfMoim(moimId)));
    }

    @Override
    @GetMapping("/{moimId}/topic")
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
    public ResponseEntity<SuccessResponse<MoimInfoOwnerResponse>> getMoimInfoForOwner(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_INFO_FOR_OWNER_GET_SUCCESS, moimService.getMoimInfoForOwner(moimId, principalHandler.getUserIdFromPrincipal())));
    }

    @Override
    @PostMapping("/{moimId}/topic")
    public ResponseEntity<SuccessResponse> createTopicOfMoim(
            @MoimIdPathVariable final Long moimId,
            @RequestBody final TopicCreateRequest createRequest,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.created(URI.create(moimService.createTopic(moimId, principalHandler.getUserIdFromPrincipal(), createRequest))).body(SuccessResponse.of(SuccessMessage.TOPIC_CREATE_SUCCESS));
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
            @PathVariable("moimId") final String moimUrl
    ) {
        return SuccessResponse.of(SuccessMessage.IS_TEMPORARY_POST_EXIST_GET_SUCCESS,
                moimService.getTemporaryPost(moimId, principalHandler.getUserIdFromPrincipal()));
    }


    @Override
    @GetMapping("/{moimId}/admin/topics")
    public ResponseEntity<SuccessResponse<MoimTopicInfoListResponse>> getMoimTopicList(
            @MoimIdPathVariable final Long moimId,
            @RequestParam final int page,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_TOPIC_LIST_GET_SUCCESS, moimService.getMoimTopicList(moimId, principalHandler.getUserIdFromPrincipal(), page)));
    }

    @Override
    @PutMapping("/{moimId}/info")
    public ResponseEntity<SuccessResponse> modifyMoimInformation(
            @MoimIdPathVariable final Long moimId,
            @RequestBody final MoimInfoModifyRequest request,
            @PathVariable("moimId") final String moimUrl
    ) {
        moimService.modifyMoimInforation(moimId, principalHandler.getUserIdFromPrincipal(), request);
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
    public ResponseEntity<SuccessResponse<MoimCreateResponse>> createMoim(
            @RequestBody final MoimCreateRequest creatRequest
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_CREATE_SUCCESS, moimService.createMoim(principalHandler.getUserIdFromPrincipal(), creatRequest)));
    }

    @GetMapping("/{moimId}/invitation-code")
    @Override
    public ResponseEntity<SuccessResponse<InvitationCodeGetResponse>> getInvitationCode(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.INVITATION_CODE_GET_SUCCESS, moimService.getInvitationCode(moimId, principalHandler.getUserIdFromPrincipal())));
    }

    @Override
    @GetMapping("/{moimId}/writernames")
    public ResponseEntity<SuccessResponse<MoimWriterNameListGetResponse>> getWriterNameListOfMoim(
            @MoimIdPathVariable final Long moimId,
            @RequestParam final int page,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_WRITERNAME_LIST_GET_SUCCESS, moimService.getWriterNameListOfMoim(moimId, principalHandler.getUserIdFromPrincipal(), page)));
    }

    @Override
    @GetMapping("/{moimId}/writername")
    public ResponseEntity<SuccessResponse<WriterNameShortResponse>> getWriterNameOfUser(
            @MoimIdPathVariable final Long moimId,
            @PathVariable("moimId") final String moimUrl
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.WRITER_NAME_GET_SUCCESS, moimService.getWriterNameOfUser(moimId, principalHandler.getUserIdFromPrincipal())));
    }

}
