package com.mile.controller.recommend;

import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.recommend.service.RecommendService;
import com.mile.recommend.service.dto.RecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendController implements RecommendControllerSwagger{

    private final RecommendService recommendService;

    @Override
    @GetMapping("/topic")
    public SuccessResponse<RecommendResponse> getRecommendationDaily() {
        return SuccessResponse.of(SuccessMessage.RECOMMENDATION_GET_SUCCESS, recommendService.getRandomRecommendation());
    }
}
