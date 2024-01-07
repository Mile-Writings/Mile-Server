package com.mile.topic.serivce;

import com.mile.moim.serivce.MoimService;
import com.mile.topic.domain.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final MoimService moimService;

    public void authenticateUserWithTopic(
            final Topic topic,
            final Long userId
    ) {
        moimService.authenticateUserOfMoim(topic.getMoim().getId(), userId);
    }

}
