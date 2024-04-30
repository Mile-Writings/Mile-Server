package com.mile.moim.domain;

import com.mile.config.BaseTimeEntity;
import com.mile.writername.domain.WriterName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Moim extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private WriterName owner;
    private String name;
    private String imageUrl;
    @Column(length = 500)
    private String information;
    private String idUrl;
    private boolean isPublic;
}
