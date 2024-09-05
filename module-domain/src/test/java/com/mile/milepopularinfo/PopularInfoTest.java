package com.mile.milepopularinfo;

import com.mile.curious.repository.dto.PostAndCuriousCountInLastWeek;
import com.mile.curious.service.CuriousRetriever;
import com.mile.moim.domain.Moim;
import com.mile.moim.domain.popular.MoimCuriousPost;
import com.mile.moim.domain.popular.MoimCuriousWriter;
import com.mile.moim.domain.popular.MoimPopularInfo;
import com.mile.moim.repository.MoimPopularInfoRepository;
import com.mile.moim.service.MoimRetriever;
import com.mile.moim.service.MoimService;
import com.mile.moim.service.dto.response.MoimCuriousPostListResponse;
import com.mile.moim.service.dto.response.MoimInfoResponse;
import com.mile.moim.service.dto.response.MoimMostCuriousPostResponse;
import com.mile.moim.service.dto.response.MoimMostCuriousWriterResponse;
import com.mile.moim.service.dto.response.MoimOverallInfoResponse;
import com.mile.post.domain.Post;
import com.mile.topic.domain.Topic;
import com.mile.utils.DateUtil;
import com.mile.writername.domain.WriterName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PopularInfoTest {

    @InjectMocks
    private MoimService moimService;

    @Mock
    private CuriousRetriever curiousRetriever;

    @Mock
    private MoimPopularInfoRepository moimPopularInfoRepository;

    @Mock
    private MoimRetriever moimRetriever;

    @Test
    @DisplayName("좋아요가 눌린 게시글이 존재할 때 moimService 에서 예측한 로직대로 리턴한다.")
    void test() {
        Moim moim = Moim.builder().name("제목").imageUrl("sss").information("test").isPublic(true).build();


        WriterName writer1 = WriterName.builder().moim(moim).name("테스트유저1").build();
        moim.setOwner(writer1);
        WriterName writer2 = WriterName.builder().moim(moim).name("테스트유저2").build();
        WriterName writer3 = WriterName.builder().moim(moim).name("테스트유저3").build();


        Topic topic = Topic.builder().content("테스트").moim(moim).content("테스트").keyword("테스트").build();

        Post post1 = Post.builder().id(1L).idUrl("MQ==").title("제목1").content("내용1").topic(topic).writerName(writer1).build();
        Post post2 = Post.builder().id(2L).idUrl("MQ==").title("제목2").content("내용2").topic(topic).writerName(writer2).build();
        Post post3 = Post.builder().id(3L).idUrl("MQ==").title("제목3").content("내용3").topic(topic).writerName(writer3).build();


        List<PostAndCuriousCountInLastWeek> mockResult = List.of(
                new PostAndCuriousCountInLastWeek(post1, 10L),
                new PostAndCuriousCountInLastWeek(post2, 2L),
                new PostAndCuriousCountInLastWeek(post3, 1L)

        );

        MoimCuriousPost moimCuriousPost = MoimCuriousPost.of(post1);
        MoimCuriousPost moimCuriousPost1 = MoimCuriousPost.of(post2);

        MoimCuriousWriter moimCuriousWriter = MoimCuriousWriter.of(writer1);
        MoimCuriousWriter moimCuriousWriter1 = MoimCuriousWriter.of(writer2);

        MoimPopularInfo moimPopularInfo = MoimPopularInfo.of(1L,
                List.of(moimCuriousPost, moimCuriousPost1),
                List.of(moimCuriousWriter, moimCuriousWriter1));
        LocalDateTime date = LocalDateTime.now();

        MoimInfoResponse moimInfoResponse = MoimInfoResponse.of(moim.getImageUrl(),
                moim.getName(),
                moim.getOwner().getName(),
                moim.getInformation(),
                0,
                DateUtil.getStringDateOfLocalDate(date));

        when(curiousRetriever.findMostCuriousPostsInLastWeek(moim)).thenReturn(mockResult);
        when(moimRetriever.getMoimInfoForTotal(moim, 0)).thenReturn(moimInfoResponse);
        when(moimRetriever.findById(1L)).thenReturn(moim);
        when(moimPopularInfoRepository.save(any())).thenReturn(moimPopularInfo);

        MoimMostCuriousWriterResponse mostCuriousWriterResponse = MoimMostCuriousWriterResponse.of(moimPopularInfo.getWriters());
        MoimCuriousPostListResponse moimCuriousPostListResponse = MoimCuriousPostListResponse.of(
                moimPopularInfo.getPosts().stream().map(MoimMostCuriousPostResponse::of).toList()
        );
        MoimOverallInfoResponse expected = new MoimOverallInfoResponse(moimInfoResponse, moimCuriousPostListResponse, mostCuriousWriterResponse);

        MoimOverallInfoResponse result = moimService.getMoimTotalInformation(1L);

        assertThat(result).isEqualTo(expected);

    }
}
