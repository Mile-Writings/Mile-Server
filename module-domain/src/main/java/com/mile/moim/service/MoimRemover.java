package com.mile.moim.service;

import com.mile.moim.domain.Moim;
import com.mile.moim.repository.MoimRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class MoimRemover {

    private final MoimRepository moimRepository;

    @Transactional
    public void deleteMoim(
            final Moim moim
    ) {

        moimRepository.delete(moim);

    }
}
