package com.mile.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mile.client.SocialType;
import com.mile.common.auth.JwtTokenProvider;
import com.mile.moim.domain.Moim;
import com.mile.moim.repository.MoimRepository;
import com.mile.moim.service.MoimService;
import com.mile.moim.service.dto.MoimCreateRequest;
import com.mile.moim.service.dto.MoimInfoModifyRequest;
import com.mile.moim.service.dto.TopicCreateRequest;
import com.mile.moim.service.dto.WriterMemberJoinRequest;
import com.mile.topic.domain.Topic;
import com.mile.topic.repository.TopicRepository;
import com.mile.user.domain.User;
import com.mile.user.repository.UserRepository;
import com.mile.utils.SecureUrlUtil;
import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
public class MoimControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecureUrlUtil secureUrlUtil;
    @Autowired
    private MoimService moimService;
    @Autowired
    private MoimRepository moimRepository;
    @Autowired
    private WriterNameRepository writerNameRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String AUTHORIZATION = "Authorization";
    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int BAD_REQUEST = 400;
    private static Long USER_ID;
    private static String MOIM_ID;
    private static String randomString;


    @BeforeEach
    @Transactional
    public void setUp() {
        randomString = UUID.randomUUID().toString().substring(0, 6);
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

        Topic topic = topicRepository.saveAndFlush(Topic.create(moim, new TopicCreateRequest(randomString, randomString.substring(0,4), randomString)));
        MOIM_ID = secureUrlUtil.encodeUrl(moim.getId());
    }

    /*
    공통 유저 플로우
     */
    @Test
    @DisplayName("베스트 모임이 정상적으로 조회된다.")
    public void getBestMoim() throws Exception {
        //given
        String requestUri = "/api/moim/best";

        //when
        MvcResult result = mockMvc.perform(
                get(requestUri)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
    }

    @Test
    @DisplayName("pathVariable 값에 잘못된 값이 들어갈 경우 400 리스폰스를 남긴다.")
    public void parameterErrorTest() throws Exception {
        //given
        String variable = UUID.randomUUID().toString();
        String requestUri = "/api/moim/" + variable;

        //when
        MvcResult result = mockMvc.perform(
                get(requestUri)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(BAD_REQUEST);

    }

    /*
    글모임의 유저 플로우
     */
    @Test
    @DisplayName("모임의 인기 게시물이 정상적으로 조회된다.")
    public void getBestPostOfMoim() throws Exception {
        //given
        String requestUri = "/api/moim/" + MOIM_ID + "/posts/top-rank";

        //when
        MvcResult result = mockMvc.perform(
                get(requestUri)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
    }

    @Test
    @DisplayName("모임 초대 코드가 정상적으로 조회된다.")
    public void getMoimInvitationCodeTest() throws Exception {
        //given
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(USER_ID);
        String requestUri = "/api/moim/" + MOIM_ID + "/invitation-code";

        //when
        MvcResult result = mockMvc.perform(
                get(requestUri)
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
    }

    @Test
    @DisplayName("글모임의 주제들이 정상적으로 조회된다.")
    public void getTopicFromMoimTest() throws Exception {
        //given
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(USER_ID);
        String requestUri = "/api/moim/" + MOIM_ID;

        //when
        MvcResult result = mockMvc.perform(
                get(requestUri)
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
    }


    @Test
    @DisplayName("글모임에 정상적으로 가입된다.")
    public void joinMoimTest() throws Exception {
        //given
        String randomShortString = UUID.randomUUID().toString().substring(0, 6);
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(USER_ID);
        String requestUri = "/api/moim/" + MOIM_ID + "/user";
        String requestBody = objectMapper.writeValueAsString(
                WriterMemberJoinRequest.of(
                        randomShortString,
                        randomShortString
                )
        );

        //when
        MvcResult result = mockMvc.perform(
                        post(requestUri)
                                .header(AUTHORIZATION, token)
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(CREATED);
    }

    @Test
    @DisplayName("글모임 초대 정보가 정상적으로 반환된다")
    @Transactional
    public void getInvitationInfoTest() throws Exception {
        //given
        String requestUri = "/api/moim/" + MOIM_ID + "/info";
        //when
        MvcResult result = mockMvc.perform(
                get(requestUri)
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
        assertThat(result.getResponse().getContentAsString()).contains(getMoimInfo(MOIM_ID));
    }

    @Test
    @DisplayName("오늘의 주제가 정상적으로 반환된다.")
    public void getTodayTopicTest() throws Exception {
        //given
        String requestUri = "/api/moim/" + MOIM_ID + "/topic/today";


        //when
        MvcResult result = mockMvc.perform(
                get(requestUri)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn();


        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
    }

    @Test
    @DisplayName("글모임의 공개상태가 정상적으로 반환된다.")
    public void getPublicStatusTest() throws Exception {
        //given
        String requestUri = "/api/moim/" + MOIM_ID + "/public-status";

        //when
        MvcResult result = mockMvc.perform(
                get(requestUri)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn();
        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
        assertThat(result.getResponse().getContentAsString()).contains("true");
    }


    @Test
    @DisplayName("글모임의 임시저장 글이 정상적으로 반환된다.")
    public void getTemporaryPostOfMoim() throws Exception {
        //given
        String requestUri = "/api/moim/" + MOIM_ID + "/temporary";
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(USER_ID);

        //when
        MvcResult result = mockMvc.perform(
                get(requestUri)
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
        assertThat(result.getResponse().getContentAsString()).contains("false");
    }

    @Test
    @DisplayName("글 모임 뷰의 본인 필명이 정상적으로 반환된다.")
    public void getWriterNameForUserTest() throws Exception {
        //given
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(USER_ID);
        String requestUri = "/api/moim/" + MOIM_ID + "/writername";

        //when
        MvcResult result = mockMvc.perform(
                get(requestUri)
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
        assertThat(result.getResponse().getContentAsString()).contains(randomString);
    }

    /*
    관리자 유저 플로우
     */
    @Test
    @DisplayName("모임이 정상적으로 생성된다.")
    public void postMoimTest() throws Exception {
        //given
        String randomString = UUID.randomUUID().toString().substring(0, 7);
        String randemTagString = UUID.randomUUID().toString().substring(0, 3);
        User user = userRepository.save(User.of(randomString, randomString, SocialType.GOOGLE));
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(user.getId());
        String createRequest = objectMapper.writeValueAsString(
                new MoimCreateRequest(
                        randomString,
                        randomString,
                        true,
                        randomString,
                        randomString,
                        randomString,
                        randomString,
                        randemTagString,
                        randomString
                ));
        String requestUri = "/api/moim";

        //when
        MvcResult result = mockMvc.perform(
                        post(requestUri)
                                .header(AUTHORIZATION, token)
                                .content(createRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
    }

    @Test
    @DisplayName("관리자 페이지 글감 조회가 정상적으로 반환된다.")
    public void getTopicForOwnerTest() throws Exception {
        //given
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(USER_ID);
        String requestUri = "/api/moim/" + MOIM_ID + "/admin/topics";


        //when
        MvcResult result = mockMvc.perform(
                get(requestUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, token)
                        .param("page", "1")
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
        assertThat(result.getResponse().getContentAsString()).contains(randomString);
    }

    @Test
    @DisplayName("관리자 페이지의 필명 조회가 정상적으로 반횐된다.")
    @Transactional
    public void getWriterNamesForOwnerTest() throws Exception {
        //given
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(USER_ID);
        String requestUri = "/api/moim/" + MOIM_ID + "/writernames";

        //when
        MvcResult result = mockMvc.perform(
                get(requestUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, token)
                        .param("page", "1")
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);

    }

    @Test
    @DisplayName("글모임 정보가 정상적으로 수정된다.")
    public void putMoimInfoTest() throws Exception {
        //given
        String randomTagString = UUID.randomUUID().toString().substring(0, 3);
        String requestUri = "/api/moim/" + MOIM_ID + "/info";
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(USER_ID);

        String request = objectMapper.writeValueAsString(new MoimInfoModifyRequest(
                randomString,
                randomTagString,
                randomString,
                true
        ));

        //when
        MvcResult result = mockMvc.perform(
                put(requestUri)
                        .content(request)
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
    }

    @Test
    @DisplayName("글감을 정상적으로 등록한다.")
    public void createTopicTest() throws Exception {
        //given
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(USER_ID);
        String requestUri = "/api/moim/" + MOIM_ID + "/topic";
        String request = objectMapper.writeValueAsString(
                TopicCreateRequest.of(randomString, "---", randomString));
        //when
        MvcResult result = mockMvc.perform(
                post(requestUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header(AUTHORIZATION, token)
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(CREATED);
    }

    @Test
    @DisplayName("글모임이 정상적으로 삭제된다.")
    public void deleteMoimTest() throws Exception {
        //given
        String token = "Bearer " + jwtTokenProvider.issueAccessToken(USER_ID);
        String requestUri = "/api/moim/" + MOIM_ID;

        //when
        MvcResult result = mockMvc.perform(
                delete(requestUri)
                        .header(AUTHORIZATION, token)
        ).andDo(print()).andReturn();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(OK);
    }


    private String getMoimInfo(String moimId) throws JsonProcessingException {
        return objectMapper.writeValueAsString(moimService.getMoimInfo(secureUrlUtil.decodeUrl(moimId)));
    }

}
