package com.mile.cocurrency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mile.common.auth.JwtTokenProvider;
import com.mile.post.service.dto.PostCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class DuplicatedInterceptorTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Test
    @DisplayName("같은 사용자가 동시에 중복된 요청을 보내면 429 에러를 반환한다.")
    public void multipleRequestTest() throws Exception {
        // given
        int numberOfThread = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
        CountDownLatch latch = new CountDownLatch(numberOfThread);
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(1L);

        PostCreateRequest bodyDto = new PostCreateRequest(
                "MQ==",
                "MQ==",
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