package com.mile.writername.domain;

import com.mile.moim.domain.Moim;
import com.mile.moim.service.dto.WriterMemberJoinRequest;
import com.mile.user.domain.User;
import com.mile.writername.service.dto.WriterNameDescriptionUpdateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "writerName", uniqueConstraints = @UniqueConstraint(columnNames = "normalizedName"))
public class WriterName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Moim moim;

    private String name;
    private String information;
    private String normalizedName;

    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    private Integer totalCuriousCount;

    @PrePersist
    @PreUpdate
    public void normalizeName() {
        this.normalizedName = this.name.replaceAll("\\s+", "").toLowerCase();
    }

    public void increaseTotalCuriousCount() {
        totalCuriousCount++;
    }

    public void decreaseTotalCuriousCount() {
        totalCuriousCount--;
        validateTotalCuriousCount();
    }

    private void validateTotalCuriousCount() {
        if( this.totalCuriousCount < 0 ) {
            this.totalCuriousCount = 0;
        }
    }

    public void decreaseTotalCuriousCountByPostDelete(final int count) {
        this.totalCuriousCount -= count;
        validateTotalCuriousCount();
    }

    public void updateInformation(WriterNameDescriptionUpdateRequest request) {
        this.information = request.description();
    }
    @Builder
    private WriterName(
            final Moim moim,
            final String name,
            final User user,
            final String information,
            final String normalizedName,
            final int curiousCount
    ) {
        this.moim = moim;
        this.name = name;
        this.normalizedName = normalizedName;
        this.writer = user;
        this.information = information;
        this.totalCuriousCount = curiousCount;
    }

    public static WriterName of(
            final Moim moim,
            final WriterMemberJoinRequest joinRequest,
            final User user
    ) {
        String normalizedWriterName = joinRequest.writerName().replaceAll("\\s+", "").toLowerCase();
        return WriterName.builder()
                .moim(moim)
                .name(joinRequest.writerName())
                .normalizedName(normalizedWriterName)
                .information(joinRequest.writerDescription())
                .user(user)
                .curiousCount(0)
                .build();
    }

    public void setMoimNull() {
        this.moim = null;
    }
}
