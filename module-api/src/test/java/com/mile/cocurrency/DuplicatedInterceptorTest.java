package com.mile.cocurrency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mile.client.SocialType;
import com.mile.common.auth.JwtTokenProvider;
import com.mile.moim.domain.Moim;
import com.mile.moim.repository.MoimRepository;
import com.mile.moim.service.dto.MoimCreateRequest;
import com.mile.moim.service.dto.TopicCreateRequest;
import com.mile.moim.service.dto.WriterMemberJoinRequest;
import com.mile.post.service.dto.PostCreateRequest;
import com.mile.topic.domain.Topic;
import com.mile.topic.repository.TopicRepository;
import com.mile.user.domain.User;
import com.mile.user.repository.UserRepository;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class DuplicatedInterceptorTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecureUrlUtil secureUrlUtil;
    @Autowired
    private MoimRepository moimRepository;
    @Autowired
    private WriterNameRepository writerNameRepository;
    @Autowired
    private TopicRepository topicRepository;

    private static Long USER_ID;
    private static String MOIM_ID;
    private static String TOPIC_ID;

    @BeforeEach
    @Transactional
    public void setUp() {
        String randomString = UUID.randomUUID().toString().substring(0, 3);
        User user = userRepository.saveAndFlush(User.of(randomString, randomString, SocialType.GOOGLE));
        USER_ID = user.getId();
        // setting
        MoimCreateRequest createRequest = new MoimCreateRequest(
                randomString,
                randomString,
                true,
                randomString,
                randomString,
                randomString,
                randomString,
                randomString,
                randomString
        );

        Moim moim = moimRepository.saveAndFlush(Moim.create(createRequest));
        WriterName writerName = writerNameRepository.saveAndFlush(WriterName.of(moim, new WriterMemberJoinRequest(randomString, randomString), user));
        moim.setOwner(writerName);
        moimRepository.saveAndFlush(moim);

        Topic topic = topicRepository.saveAndFlush(Topic.create(moim, new TopicCreateRequest(randomString, randomString, randomString)));
        MOIM_ID = secureUrlUtil.encodeUrl(moim.getId());

        TOPIC_ID = secureUrlUtil.encodeUrl(topic.getId());
    }

    @Test
    @DisplayName("같은 사용자가 동시에 중복된 요청을 보내면 429 에러를 반환한다.")
    public void multipleRequestTest() throws Exception {

        // given
        int numberOfThread = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
        CountDownLatch latch = new CountDownLatch(numberOfThread);
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(USER_ID);

        PostCreateRequest bodyDto = new PostCreateRequest(
                MOIM_ID,
                TOPIC_ID,
                "string",
                "string",
                "string",
                false
        );

        String body = objectMapper.writeValueAsString(bodyDto);

        // when
        List<MvcResult> results = new ArrayList<>();

        for (int i = 0; i < numberOfThread; i++) {
            executorService.submit(() -> {
                try {
                    MvcResult result = mockMvc.perform(
                                    post("/api/post")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .header("Authorization", token)
                                            .content(body)
                            )
                            .andDo(print())
                            .andReturn();
                    synchronized (results) {
                        results.add(result);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        int count200 = 0;
        int count429 = 0;
        for (MvcResult mvcResult : results) {
            if (mvcResult.getResponse().getStatus() == HttpStatus.OK.value()) count200++;
            else if (mvcResult.getResponse().getStatus() == HttpStatus.TOO_MANY_REQUESTS.value()) count429++;
        }

        assertThat(count200).isEqualTo(1);
        assertThat(count429).isEqualTo(numberOfThread - 1);
    }
}