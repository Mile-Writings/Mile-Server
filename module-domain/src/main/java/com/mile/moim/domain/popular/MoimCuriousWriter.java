package com.mile.moim.domain.popular;

import com.mile.writername.domain.WriterName;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimCuriousWriter {
    Long writerNameId;
    String name;


    private MoimCuriousWriter(final Long writerNameId, final String name) {
        this.writerNameId = writerNameId;
        this.name = name;
    }

    public static MoimCuriousWriter of(final WriterName writerName) {
        return new MoimCuriousWriter(writerName.getId(), writerName.getName());
    }

    @Override
    public int hashCode() {
        return writerNameId.intValue();
    }

    @Override
    public boolean equals(Object target) {
        MoimCuriousWriter targetWriter = (MoimCuriousWriter) target;

        return Objects.equals(this.name, targetWriter.name) && Objects.equals(this.writerNameId, targetWriter.writerNameId);
    }
}
