package com.mile.recommend.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.recommend.repository.RecommendRepository;
import com.mile.recommend.service.dto.RecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final RecommendRepository recommendRepository;
    private static Long GROUND_ID = 0L;
    private static final int INDEX = 1;

    public RecommendResponse getRandomRecommendation() {
        return RecommendResponse.of(getRandomContentDaily());
    }
    @Scheduled(cron = "0 0 0 * * *")
    private String getRandomContentDaily() {
        return recommendRepository.findById(GROUND_ID + INDEX).orElseGet(() -> {
            resetGroundId();
            return recommendRepository.findById(GROUND_ID + INDEX).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.RECCOMEND_NOT_FOUND)
            );
        }).getContent();
    }

    private void resetGroundId() {
        GROUND_ID = 0L;
    }
}