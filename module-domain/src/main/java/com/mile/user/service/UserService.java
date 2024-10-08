package com.mile.user.service;

import com.mile.client.SocialType;
import com.mile.moim.service.dto.response.MoimListOfUserResponse;
import com.mile.moim.service.dto.response.MoimOfUserResponse;
import com.mile.user.domain.User;
import com.mile.writername.domain.MoimRole;
import com.mile.writername.service.WriterNameRetriever;
import com.mile.writername.service.WriterNameService;
import com.mile.writername.service.vo.WriterNameInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRetriever userRetriever;
    private final UserRemover userRemover;
    private final UserCreator userCreator;
    private final WriterNameRetriever writerNameRetriever;
    private final WriterNameService writerNameService;

    public User findById(final Long userId) {
        return userRetriever.findById(userId);
    }

    public boolean isExistingUser(final String socialId, final SocialType socialType) {
        return userRetriever.isExistingUser(socialId, socialType);
    }

    public Long createUser(final String socialId, final SocialType socialType, final String email) {
        return userCreator.createUser(socialId, socialType, email);
    }

    public User getBySocialId(final String socialId, final SocialType socialType) {
        return userRetriever.getBySocialId(socialId, socialType);
    }

    public void deleteUser(final Long userId) {
        User user = userRetriever.findById(userId);
        writerNameService.deleteWriterNameByUser(user);
        userRemover.delete(user);
    }

    public MoimListOfUserResponse getMoimOfUserList(final Long userId) {
        return MoimListOfUserResponse.of(writerNameRetriever.getMoimListOfUser(userId)
                .stream()
                .map(MoimOfUserResponse::of)
                .collect(Collectors.toList()));
    }

    public Map<Long, WriterNameInfo> getJoinedRoleFromUser(final Long userId) {
        return writerNameRetriever.getJoinedRoleFromUserId(userId);
    }
}
