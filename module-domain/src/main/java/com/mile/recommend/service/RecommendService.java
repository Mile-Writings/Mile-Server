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
    private static final Long INDEX = 1L;

    public RecommendResponse getRandomRecommendation() {
        return RecommendResponse.of(
                recommendRepository.findById(GROUND_ID).orElseGet(() -> {
                    resetGroundId();
                    return recommendRepository.findById(GROUND_ID).orElseThrow(
                            () -> new NotFoundException(ErrorMessage.RECOMMEND_NOT_FOUND)
                    );
                }
        ).getContent());
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void increaseId() {
        GROUND_ID += INDEX;
    }

    private void resetGroundId() {
        GROUND_ID = 1L;
    }
}