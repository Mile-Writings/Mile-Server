package com.mile.topic.serivce;

import com.mile.config.BaseTimeEntity;
import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.topic.domain.Topic;
import com.mile.topic.repository.TopicRepository;
import com.mile.topic.serivce.dto.ContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;


    public List<ContentResponse> getContentsFromMoim(
            final Long moimId
    ) {
        List<Topic> topicList = sortByCreatedAt(findByMoimId(moimId));
        isContentsEmpty(topicList);
        return topicList
                .stream()
                .map(ContentResponse::of)
                .collect(Collectors.toList());
    }

    private void isContentsEmpty(
            final List<Topic> topicList
    ) {
        if (topicList.isEmpty()) {
            throw new NotFoundException(ErrorMessage.CONTENT_NOT_FOUND);
        }
    }

    private List<Topic> findByMoimId(
            final Long moimId
    ) {
        return topicRepository.findByMoimId(moimId);
    }

    private List<Topic> sortByCreatedAt(
            final List<Topic> topicList
    ) {
        return topicList.stream()
                .sorted(Comparator.comparing(BaseTimeEntity::getCreatedAt))
                .collect(Collectors.toList());
    }

    public Topic findById(
            final Long topicId
    ) {
        return topicRepository.findById(topicId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.TOPIC_NOT_FOUND)
                );
    }

    public String findLatestTopicByMoim(
            final Moim moim
    ) {
        return topicRepository.findLatestTopicByMoim(moim)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.MOIM_TOPIC_NOT_FOUND)
                );
    }
}
