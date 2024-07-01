package com.mile.moim.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ForbiddenException;
import com.mile.exception.model.NotFoundException;
import com.mile.moim.domain.Moim;
import com.mile.moim.repository.MoimRepository;
import com.mile.moim.service.lock.AtomicValidateUniqueMoimName;
import com.mile.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MoimRetriever {

    private final MoimRepository moimRepository;

    public Moim findById(
            final Long moimId
    ) {
        return moimRepository.findById(moimId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MOIM_NOT_FOUND)
        );
    }

    public void authenticateOwnerOfMoim(
            final Moim moim,
            final User user
    ) {
        if (!isMoimOwnerEqualsUser(moim, user)) {
            throw new ForbiddenException(ErrorMessage.MOIM_OWNER_AUTHENTICATION_ERROR);
        }
    }

    public boolean isMoimOwnerEqualsUser(
            final Moim moim,
            final User user
    ) {
        return moim.getOwner().getWriter().equals(user);
    }

    public List<Moim> findBestMoims() {
        LocalDateTime endOfWeek = LocalDateTime.now();
        LocalDateTime startOfWeek = endOfWeek.minusDays(7);
        PageRequest pageRequest = PageRequest.of(0, 2);
        return moimRepository.findTop3PublicMoimsWithMostPostsLastWeek(pageRequest, startOfWeek, endOfWeek);
    }

    public List<Moim> getLatestMoims(int count, List<Moim> excludeMoims) {
        PageRequest pageRequest = PageRequest.of(0, count);
        if (excludeMoims.isEmpty()) {
            return moimRepository.findLatestMoimsWithoutExclusion(pageRequest);
        } else {
            return moimRepository.findLatestMoimsWithExclusion(pageRequest, excludeMoims);
        }
    }


    @AtomicValidateUniqueMoimName
    public boolean checkNormalizeName(final String normalizedName) {
        return moimRepository.existsByNormalizedName(normalizedName);
    }

}
