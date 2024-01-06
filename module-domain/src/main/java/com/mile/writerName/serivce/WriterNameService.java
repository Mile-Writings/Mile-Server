package com.mile.writerName.serivce;

import com.mile.writerName.repository.WriterNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriterNameService {
    private final WriterNameRepository penNameRepository;

    public boolean isUserInMoim(
            final Long moimId,
            final Long writerId
    ) {
        return penNameRepository.findByMoimIdAndWriterId(moimId, writerId).isPresent();
    }

}
