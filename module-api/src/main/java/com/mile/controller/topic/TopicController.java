package com.mile.controller.topic;

import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.resolver.topic.TopicIdPathVariable;
import com.mile.topic.service.TopicService;
import com.mile.topic.service.dto.PostListInTopicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/topic")
public class TopicController implements TopicControllerSwagger {

    private final TopicService topicService;

    @Override
    @GetMapping("/{topicId}")
    public SuccessResponse<PostListInTopicResponse> getPostListByTopic(
            @TopicIdPathVariable Long topicId,
            @PathVariable("topicId") final String topicUrl
    ) {
        return SuccessResponse.of(SuccessMessage.MOIM_POST_GET_SUCCESS, topicService.getPostListByTopic(topicId));
    }
}
