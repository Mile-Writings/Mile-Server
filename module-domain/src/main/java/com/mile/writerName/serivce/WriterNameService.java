package com.mile.writerName.serivce;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.NotFoundException;
import com.mile.writerName.domain.WriterName;
import com.mile.writerName.repository.WriterNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WriterNameService {
    private final WriterNameRepository writerNameRepository;

    public boolean isUserInMoim(
            final Long moimId,
            final Long writerId
    ) {
        return writerNameRepository.findByMoimIdAndWriterId(moimId, writerId).isPresent();
    }

    public WriterName findByMoimAndUser(
            final Long moimId,
            final Long writerId
    ) {
        return writerNameRepository.findByMoimIdAndWriterId(moimId, writerId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.USER_AUTHENTICATE_ERROR)
                );
    }
}
