package com.mile.controller.moim;

import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.moim.serivce.MoimService;
import com.mile.moim.serivce.dto.ContentListResponse;
import com.mile.moim.serivce.dto.MoimInfoResponse;
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

    @GetMapping("/{moimId}")
    @Override
    public SuccessResponse<ContentListResponse> getTopicsFromMoim(
            @PathVariable final Long moimId,
            final Principal principal
    ) {
        return SuccessResponse.of(SuccessMessage.TOPIC_SEARCH_SUCCESS, moimService.getContentsFromMoim(moimId, Long.valueOf(principal.getName())));
    }

    @GetMapping("/{moimId}/authenticate")
    @Override
    public SuccessResponse getAuthenticationOfMoim(
            final Long moimId,
            final Principal principal
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_AUTHENTICATE_SUCCESS, moimService.getAuthenticateUserOfMoim(moimId, Long.valueOf(principal.getName())));
    }

    @GetMapping("/{moimId}/info")
    @Override
    public SuccessResponse<MoimInfoResponse> getMoimInfo(
            final Long moimId
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_INFO_SUCCESS, moimService.getMoimInfo(moimId));
    }
}
