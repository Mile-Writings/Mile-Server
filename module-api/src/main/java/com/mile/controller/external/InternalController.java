package com.mile.controller.external;

import com.mile.common.CacheService;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.moim.service.MoimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InternalController {

    private final CacheService cacheService;
    private final MoimService moimService;

    @PostMapping("/api/v1/moim/info/cache")
    public void deleteMoimInfoCache() {
        cacheService.deleteMoimCache();
    }

    @GetMapping("/api/internal/post-data")
    public ResponseEntity getPostAndMoimIdForMetaData() {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_POST_MAP_GET_SUCCESS, moimService.getAllPostMoimMap()));
    }
}
