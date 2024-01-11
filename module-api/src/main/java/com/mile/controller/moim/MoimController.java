package com.mile.controller.moim;

import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.moim.service.MoimService;
import com.mile.moim.service.dto.CategoryListResponse;
import com.mile.moim.service.dto.ContentListResponse;
import com.mile.moim.service.dto.MoimCuriousPostListResponse;
import com.mile.moim.service.dto.MoimInfoResponse;
import com.mile.moim.service.dto.MoimTopicResponse;
import com.mile.writerName.service.dto.PopularWriterListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/moim")
public class MoimController implements MoimControllerSwagger {

    private final MoimService moimService;

    @Override
    @GetMapping("/{moimId}")
    public SuccessResponse<ContentListResponse> getTopicsFromMoim(
            @PathVariable final Long moimId,
            final Principal principal
    ) {
        return SuccessResponse.of(SuccessMessage.TOPIC_SEARCH_SUCCESS, moimService.getContentsFromMoim(moimId, Long.valueOf(principal.getName())));
    }

    @Override
    @GetMapping("/{moimId}/authenticate")
    public SuccessResponse getAuthenticationOfMoim(
            @PathVariable final Long moimId,
            final Principal principal
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_AUTHENTICATE_SUCCESS, moimService.getAuthenticateUserOfMoim(moimId, Long.valueOf(principal.getName())));
    }

    @Override
    @GetMapping("/{moimId}/mostCuriousWriters")
    public SuccessResponse<PopularWriterListResponse> getMostCuriousWritersOfMoim(
            @PathVariable final Long moimId
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_POPULAR_WRITER_SEARCH_SUCCESS, moimService.getMostCuriousWriters(moimId));
    }

    @Override
    @GetMapping("/{moimId}/topic")
    public SuccessResponse<MoimTopicResponse> getTopicFromMoim(
            @PathVariable Long moimId
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_TOPIC_GET_SUCCESS, moimService.getTopicFromMoim(moimId));

    }

    @GetMapping("/{moimId}/info")
    @Override
    public SuccessResponse<MoimInfoResponse> getMoimInfo(
            @PathVariable final Long moimId
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_INFO_SUCCESS, moimService.getMoimInfo(moimId));
    }


    @GetMapping("/{moimId}/categoryList")
    @Override
    public SuccessResponse<CategoryListResponse> getCategoryList(
            @PathVariable final Long moimId
    ) {
        return SuccessResponse.of(SuccessMessage.CATEGORY_LIST_SEARCH_SUCCESS, moimService.getCategoryList(moimId));
    }

    @GetMapping("/{moimId}/mostCuriousPost")
    public SuccessResponse<MoimCuriousPostListResponse> getMostCuriousPostByMoim(
            @PathVariable final Long moimId
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_TOP_2_POST_GET_SUCCESS, moimService.getMostCuriousPostFromMoim(moimId));
    }
}
