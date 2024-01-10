package com.mile.moim.serivce;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.moim.repository.MoimRepository;
import com.mile.moim.serivce.dto.ContentListResponse;
import com.mile.moim.serivce.dto.MoimAuthenticateResponse;
import com.mile.topic.serivce.TopicService;
import com.mile.writerName.domain.WriterName;
import com.mile.writerName.repository.WriterNameRepository;
import com.mile.writerName.serivce.WriterNameService;
import com.mile.writerName.serivce.dto.PopularWriterListResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoimService {

    private final WriterNameService writerNameService;
    private final TopicService topicService;
    private final MoimRepository moimRepository;
    private final WriterNameRepository writerNameRepository;

    private static final int NUMBER_OF_MOST_CURIOUS_WRITERS = 2;


    public ContentListResponse getContentsFromMoim(
            final Long moimId,
            final Long userId
    ) {
        authenticateUserOfMoim(moimId, userId);
        return ContentListResponse.of(topicService.getContentsFromMoim(moimId));
    }

    public void authenticateUserOfMoim(
            final Long moimId,
            final Long userId
    ) {
        if (!writerNameService.isUserInMoim(moimId, userId)) {
            throw new ForbiddenException(ErrorMessage.USER_AUTHENTICATE_ERROR);
        }
    }

    public MoimAuthenticateResponse getAuthenticateUserOfMoim(
            final Long moimId,
            final Long userId
    ) {
        return MoimAuthenticateResponse.of(writerNameService.isUserInMoim(moimId, userId));
    }

    public PopularWriterListResponse getMostCuriousWriters(
            final Long moimId
    ) {
        List<WriterName> writersOfMoim = writerNameRepository.findByMoimId(moimId);
        Map<WriterName, Integer> curiousMap = getWritersAndCuriousCount(writersOfMoim, moimId);
        List<WriterName> sortedWritersOfMoim = sortWritersByCuriousCount(writersOfMoim, curiousMap);
        List<WriterName> writers = getWriters(sortedWritersOfMoim);
        return PopularWriterListResponse.of(writers);
    }

    public Map<WriterName, Integer> getWritersAndCuriousCount(
            final List<WriterName> writersOfMoim,
            final Long moimId
    ) {
        Map<WriterName, Integer> curiousCountMap = new HashMap<>();
        for (WriterName writerName : writersOfMoim) {
            int curiousCount = writerName.getTotalCuriousCount();
            curiousCountMap.put(writerName, curiousCount);
        }
        return curiousCountMap;
    }

    public List<WriterName> sortWritersByCuriousCount(
            final List<WriterName> writersOfMoim,
            final Map<WriterName, Integer> curiousCountMap
    ) {
        Collections.sort(writersOfMoim, (writer1, writer2) -> // 오름차순 정렬
                curiousCountMap.get(writer2).compareTo(curiousCountMap.get(writer1)));
        return writersOfMoim;
    }

    public List<WriterName> getWriters(
            List<WriterName> writersOfMoim
    ) {
        if (writersOfMoim.size() == 0) {
            // throw new Exception(ErrorMessage.ERROR);
        }
        return writersOfMoim.subList(0, NUMBER_OF_MOST_CURIOUS_WRITERS);
    }
}
