package com.mile.exception.log.discord.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Author {

    private final String name;
    private final String url;
    private final String iconUrl;
}