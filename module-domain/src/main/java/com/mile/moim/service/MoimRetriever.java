package com.mile.moim.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.repository.MoimRepository;
import com.mile.user.domain.User;
import com.mile.user.service.UserService;
import com.mile.writername.service.WriterNameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MoimRetriever {

    private final WriterNameService writerNameService;
    private final MoimRepository moimRepository;
    private final UserService userService;

    public Moim findById(
            final Long moimId
    ) {
        return moimRepository.findById(moimId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MOIM_NOT_FOUND)
        );
    }

    public void getAuthenticateOwnerOfMoim(
            final Long moimId,
            final Long userId
    ) {
        Long writerNameId = writerNameService.getWriterNameIdByMoimIdAndUserId(moimId, userId);
        Moim moim = findById(moimId);
        if (!moim.getOwner().getId().equals(writerNameId)) {
            throw new ForbiddenException(ErrorMessage.MOIM_OWNER_AUTHENTICATION_ERROR);
        }
    }

    public void authenticateOwnerOfMoim(
            final Moim moim,
            final Long userId
    ) {
        if (!isMoimOwnerEqualsUser(moim, userService.findById(userId))) {
            throw new ForbiddenException(ErrorMessage.MOIM_OWNER_AUTHENTICATION_ERROR);
        }
    }

    public boolean isMoimOwnerEqualsUser(
            final Moim moim,
            final User user
    ) {
        return moim.getOwner().getWriter().equals(user);
    }

}
