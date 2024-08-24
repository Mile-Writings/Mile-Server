package com.mile.post.service.dto.response;

import com.mile.comment.service.dto.CommentResponse;
import java.util.List;

public record CommentListResponse (
        List<CommentResponse> comments
){
    public static CommentListResponse of(
            final List<CommentResponse> commentResponseList
    ) {
        return new CommentListResponse(commentResponseList);
    }
}
