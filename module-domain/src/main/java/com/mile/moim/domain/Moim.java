package com.mile.moim.domain;

import com.mile.config.BaseTimeEntity;
import com.mile.moim.service.dto.MoimCreateRequest;
import com.mile.moim.service.dto.MoimInfoModifyRequest;
import com.mile.writername.domain.WriterName;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Moim extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private WriterName owner;
    private String name;
    private String imageUrl;
    @Column(length = 500)
    private String information;
    @Setter
    private String idUrl;
    private boolean isPublic;

    public void modifyMoimInfo(
            final MoimInfoModifyRequest moimInfoModifyRequest
    ) {
        this.name = moimInfoModifyRequest.moimTitle();
        if (moimInfoModifyRequest.imageUrl() != null) {
            this.imageUrl = moimInfoModifyRequest.imageUrl();
        }
        this.information = moimInfoModifyRequest.description();
        this.isPublic = moimInfoModifyRequest.isPublic();
    }

    @Builder
    private Moim(
            final String name,
            final String imageUrl,
            final String information,
            final boolean isPublic
    ) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.information = information;
        this.isPublic = isPublic;
    }

    public static Moim create(
            final MoimCreateRequest moimCreateRequest
    ) {
        String DEFAULT_IMG_URL = "https://mile-s3.s3.ap-northeast-2.amazonaws.com/test/groupMile.png";

        return Moim.builder()
                .name(moimCreateRequest.moimName())
                .imageUrl(moimCreateRequest.imageUrl() == null ? DEFAULT_IMG_URL : moimCreateRequest.imageUrl())
                .information(moimCreateRequest.moimDescription())
                .isPublic(moimCreateRequest.isPublic())
                .build();
    }
}
