package com.mile.writerName.domain;

import com.mile.moim.domain.Moim;
import com.mile.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class WriterName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Moim moim;

    private String penName;
    private String information;

    @ManyToOne
    private User writer;
}
