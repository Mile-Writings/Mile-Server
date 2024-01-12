package com.mile.writerName.domain;

import com.mile.moim.domain.Moim;
import com.mile.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class WriterName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Moim moim;

    private String name;
    private String information;

    @ManyToOne
    private User writer;

    private Integer totalCuriousCount;

    public void increaseTotalCuriousCount() {
        totalCuriousCount++;
    }

    public void decreaseTotalCuriousCount() {
        totalCuriousCount--;
    }

    @Builder
    private WriterName(
            final Moim moim,
            final String name,
            final User user
    ) {
        this.moim = moim;
        this.name = name;
        this.writer = user;
    }

    public static WriterName of(
            final Moim moim,
            final String name,
            final User user
    ) {
        return WriterName.builder()
                .moim(moim)
                .name(name)
                .user(user).build();
    }
}
