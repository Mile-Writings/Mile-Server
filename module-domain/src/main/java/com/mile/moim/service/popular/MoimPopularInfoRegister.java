package com.mile.moim.service.popular;

import com.mile.common.lock.DistributedLock;
import com.mile.curious.repository.dto.PostAndCuriousCountInLastWeek;
import com.mile.curious.service.CuriousRetriever;
import com.mile.moim.domain.Moim;
import com.mile.moim.domain.popular.MoimCuriousPost;
import com.mile.moim.domain.popular.MoimCuriousWriter;
import com.mile.moim.domain.popular.MoimPopularInfo;
import com.mile.moim.repository.MoimPopularInfoRepository;
import com.mile.writername.domain.WriterName;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class MoimPopularInfoRegister {

    private final MoimPopularInfoRepository moimPopularInfoRepository;
    private final CuriousRetriever curiousRetriever;
    private final DistributedLock distributedLock;


    private Set<MoimCuriousPost> getMoimCuriousPost(final List<PostAndCuriousCountInLastWeek> mostCuriousPostsInLastWeek) {
        return mostCuriousPostsInLastWeek.stream().map(p ->
                MoimCuriousPost.of(p.getPost())).limit(2).collect(Collectors.toUnmodifiableSet());
    }

    private Set<MoimCuriousWriter> getMoimCuriousWriter(final List<PostAndCuriousCountInLastWeek> mostCuriousPostsInLastWeek) {
        Map<WriterName, Long> writerNameCount = mostCuriousPostsInLastWeek.stream()
                .collect(Collectors.groupingBy(p -> p.getPost().getWriterName(), Collectors.summingLong(PostAndCuriousCountInLastWeek::getCount)));

        List<WriterName> topTwoWriters = writerNameCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(2)
                .map(Map.Entry::getKey).toList();

        return topTwoWriters.stream().map(MoimCuriousWriter::of).collect(Collectors.toUnmodifiableSet());
    }

    @CachePut(value = "moimPopularInfo", key = "#moim.id")
    public MoimPopularInfo setMostPopularInfoOfMoim(final Moim moim) {
        distributedLock.getLock("MOIM_POPULAR_LOCK");

        List<PostAndCuriousCountInLastWeek> mostCuriousPostsInLastWeek = curiousRetriever.findMostCuriousPostsInLastWeek(moim);

        Set<MoimCuriousPost> moimCuriousPosts = getMoimCuriousPost(mostCuriousPostsInLastWeek);

        Set<MoimCuriousWriter> moimCuriousWriters = getMoimCuriousWriter(mostCuriousPostsInLastWeek);

        MoimPopularInfo moimPopularInfo = MoimPopularInfo.of(moim.getId(), moimCuriousPosts, moimCuriousWriters);

        moimPopularInfoRepository.saveAndFlush(moimPopularInfo);

        distributedLock.afterLock("MOIM_POPULAR_LOCK");

        return moimPopularInfo;
    }

}
