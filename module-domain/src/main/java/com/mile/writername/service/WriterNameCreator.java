package com.mile.writername.service;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.ConflictException;
import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.request.WriterMemberJoinRequest;
import com.mile.user.domain.User;
import com.mile.writername.domain.WriterName;
import com.mile.writername.repository.WriterNameRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class WriterNameCreator {
    private final WriterNameRepository writerNameRepository;

    public WriterName createWriterName(final User user, final Moim moim, final WriterMemberJoinRequest joinRequest) {
        WriterName writerName;
        try {
            writerName = writerNameRepository.save(WriterName.of(moim, joinRequest, user));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(ErrorMessage.WRITER_NAME_ALREADY_EXIST);
        }
        return writerName;
    }
}
