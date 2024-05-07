package com.mile.commentreply.service.dto;

public record ReplyCreateRequest(
        String content,
        boolean isAnonymous
) {
}
