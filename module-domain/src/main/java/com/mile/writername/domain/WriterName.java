package com.mile.writername.domain;

import com.mile.moim.domain.Moim;
import com.mile.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Moim moim;

    private String name;
    private String information;

    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    private Integer totalCuriousCount;

    public void increaseTotalCuriousCount() {
        totalCuriousCount++;
    }

    public void decreaseTotalCuriousCount() {
        totalCuriousCount--;
        setTotalCuriousCountZero();
    }

    private void setTotalCuriousCountZero() {
        if( this.totalCuriousCount < 0 ) {
            this.totalCuriousCount = 0;
        }
    }

    @Builder
    private WriterName(
            final Moim moim,
            final String name,
            final User user,
            final int curiousCount
    ) {
        this.moim = moim;
        this.name = name;
        this.writer = user;
        this.totalCuriousCount = curiousCount;
    }

    public static WriterName of(
            final Moim moim,
            final String name,
            final User user
    ) {
        return WriterName.builder()
                .moim(moim)
                .name(name)
                .user(user)
                .curiousCount(0)
                .build();
    }
}
