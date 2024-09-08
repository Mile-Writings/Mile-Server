package com.mile.moim.domain.popular;

import com.mile.writername.domain.WriterName;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimCuriousWriter {
    String name;

    private MoimCuriousWriter(final String name) {
        this.name = name;
    }

    public static MoimCuriousWriter of(final WriterName writerName) {
        return new MoimCuriousWriter(writerName.getName());
    }
}
