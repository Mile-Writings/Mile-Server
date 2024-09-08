package com.mile.moim.domain.popular;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimPopularInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "moim_id")
    private Long moimId;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<MoimCuriousPost> posts;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<MoimCuriousWriter> writers;

    private MoimPopularInfo(final Long moimId, final List<MoimCuriousPost> posts, final List<MoimCuriousWriter> writers) {
        this.moimId = moimId;
        this.posts = posts;
        this.writers = writers;
    }

    public static MoimPopularInfo of(final Long moimId, final List<MoimCuriousPost> posts, final List<MoimCuriousWriter> writers) {
        return new MoimPopularInfo(moimId, posts, writers);
    }
}
