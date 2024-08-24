package com.mile.moim.service;

import com.mile.moim.domain.Moim;
import com.mile.moim.repository.MoimRepository;
import com.mile.moim.service.dto.request.MoimCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MoimCreator {

    private final MoimRepository moimRepository;

    public Moim createMoim(MoimCreateRequest moimCreateRequest) {
        return moimRepository.save(Moim.create(moimCreateRequest));
    }
}
