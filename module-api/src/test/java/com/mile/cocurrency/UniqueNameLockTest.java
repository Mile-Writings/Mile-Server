package com.mile.cocurrency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mile.client.SocialType;
import com.mile.common.auth.JwtTokenProvider;
import com.mile.moim.service.dto.MoimCreateRequest;
import com.mile.user.domain.User;
import com.mile.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.MethodName.class)
public class UniqueNameLockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long USER_ID;

    @Test
    @DisplayName("더미 유저 생성")
    public void aTestForDummyUser() {
        String randomString = UUID.randomUUID().toString().substring(0, 7);
        User user = userRepository.saveAndFlush(User.of(randomString, randomString, SocialType.GOOGLE));
        USER_ID = user.getId();
    }

    @Test
    @DisplayName("중복 요청에 대한 Lock으로 인해 동시에 같은 제목으로 요청을 보내면 400 에러를 반환한다.")
    public void uniqueMoimNameTest() throws Exception {
        // given
        int numberOfThread = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
        CountDownLatch latch = new CountDownLatch(numberOfThread);
        String randomName = UUID.randomUUID().toString().substring(0,7);
        MoimCreateRequest bodyDto = new
                MoimCreateRequest(randomName, "string", false, "string", "string", "string", "string", "str", "string");

        String body = objectMapper.writeValueAsString(bodyDto);
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(USER_ID);


        // when
        List<MvcResult> results = new ArrayList<>();
        for (int i = 0; i < numberOfThread; i++) {
            executorService.submit(() -> {
                try {
                    MvcResult result = mockMvc.perform(
                                    post("/api/moim")
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
        int count400 = 0;
        for (MvcResult mvcResult : results) {
            if (mvcResult.getResponse().getStatus() == HttpStatus.OK.value()) count200++;
            else if (mvcResult.getResponse().getStatus() == HttpStatus.BAD_REQUEST.value()) count400++;
        }

        assertThat(count200).isEqualTo(1);
        assertThat(count400).isEqualTo(numberOfThread - 1);
    }

}

