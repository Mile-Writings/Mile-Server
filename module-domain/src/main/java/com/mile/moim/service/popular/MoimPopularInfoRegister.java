package com.mile.moim.service.popular;

import com.mile.curious.repository.dto.PostAndCuriousCountInLastWeek;
import com.mile.curious.service.CuriousRetriever;
import com.mile.moim.domain.Moim;
import com.mile.moim.domain.popular.MoimCuriousPost;
import com.mile.moim.domain.popular.MoimCuriousWriter;
import com.mile.moim.domain.popular.MoimPopularInfo;
import com.mile.moim.repository.MoimPopularInfoRepository;
import com.mile.moim.service.lock.AtomicValidateMoimPopulerInfo;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class MoimPopularInfoRegister {
    private final MoimPopularInfoRepository moimPopularInfoRepository;

    private final CuriousRetriever curiousRetriever;


    private List<MoimCuriousPost> getMoimCuriousPost(final List<PostAndCuriousCountInLastWeek> mostCuriousPostsInLastWeek) {
        return mostCuriousPostsInLastWeek.stream().map(p ->
                MoimCuriousPost.of(p.getPost())).limit(2).toList();
    }

    private List<MoimCuriousWriter> getMoimCuriousWriter(final List<PostAndCuriousCountInLastWeek> mostCuriousPostsInLastWeek) {
        Map<WriterName, Long> writerNameCount = mostCuriousPostsInLastWeek.stream()
                .collect(Collectors.groupingBy(p -> p.getPost().getWriterName(), Collectors.summingLong(PostAndCuriousCountInLastWeek::getCount)));

        List<WriterName> topTwoWriters = writerNameCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(2)
                .map(Map.Entry::getKey).toList();

        return topTwoWriters.stream().map(MoimCuriousWriter::of).toList();
    }

    @CachePut(value = "moimPopularInfo", key = "#moim.id")
    @AtomicValidateMoimPopulerInfo
    public MoimPopularInfo setMostPopularInfoOfMoim(final Moim moim) {
        List<PostAndCuriousCountInLastWeek> mostCuriousPostsInLastWeek = curiousRetriever.findMostCuriousPostsInLastWeek(moim);

        List<MoimCuriousPost> moimCuriousPosts = getMoimCuriousPost(mostCuriousPostsInLastWeek);

        List<MoimCuriousWriter> moimCuriousWriters = getMoimCuriousWriter(mostCuriousPostsInLastWeek);

        MoimPopularInfo moimPopularInfo = MoimPopularInfo.of(moim.getId(), moimCuriousPosts, moimCuriousWriters);

        return moimPopularInfoRepository.saveAndFlush(moimPopularInfo);
    }

}
