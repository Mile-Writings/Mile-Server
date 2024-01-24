package com.mile.writername.service;


import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.writername.repository.RandomWriterNamePrefixRepository;
import com.mile.writername.repository.RandomWriterNameSuffixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomWriterNameService {
    private final RandomWriterNameSuffixRepository suffixRepository;
    private final RandomWriterNamePrefixRepository prefixRepository;

    private static final int PREFIX_SEED = 115;
    private static final int SUFFIX_SEED = 192;

    public String generateRandomWriterName() {
        Random random = new Random();
        return prefixRepository.findById(Long.valueOf(random.nextInt(PREFIX_SEED) + 1)).orElseThrow(
                () -> new NotFoundException(ErrorMessage.RANDOM_VALUE_NOT_FOUND)).getPrefix() +
                suffixRepository.findById(Long.valueOf(random.nextInt(SUFFIX_SEED) + 1)).orElseThrow(
                        () -> new NotFoundException(ErrorMessage.RANDOM_VALUE_NOT_FOUND)
                ).getSuffix();

    }
}
