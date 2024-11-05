package com.mile.controller.external;

import com.mile.common.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InternalController {

    private final CacheService cacheService;

    @PostMapping("/api/v1/moim/info/cache")
    public void deleteMoimInfoCache() {
        cacheService.deleteMoimCache();
    }
}
