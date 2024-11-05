package com.mile.controller.external;

import com.mile.common.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InternalController {

    private final CacheService cacheService;

    @DeleteMapping("/api/v1/moim/info/cache")
    public void deleteMoimInfoCache() {
        cacheService.deleteMoimCache();
    }
}
