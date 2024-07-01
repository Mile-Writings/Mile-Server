package com.mile.controller.writername;

import com.mile.common.resolver.user.UserId;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.writername.service.WriterNameService;
import com.mile.writername.service.dto.WriterNameDescriptionResponse;
import com.mile.writername.service.dto.WriterNameDescriptionUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/writername")
public class WriterNameController implements WriterNameControllerSwagger {

    private final WriterNameService writerNameService;

    @Override
    @DeleteMapping("/{writerNameId}")
    public ResponseEntity<SuccessResponse> deleteMember(
            @PathVariable("writerNameId") final Long writerNameId,
            @UserId final Long userId
    ) {
        writerNameService.deleteWriterNameById(writerNameId, userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_MEMBER_DELETE_SUCCESS));
    }


    @Override
    @GetMapping("/{writerNameId}/profile")
    public ResponseEntity<SuccessResponse<WriterNameDescriptionResponse>> getWriterNameDescription(
            @PathVariable("writerNameId") final Long writerNameId,
            @UserId final Long userId
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.WRITER_NAME_GET_SUCCESS, writerNameService.findWriterNameDescription(userId, writerNameId)));
    }

    @Override
    @PatchMapping("/{writerNameId}/description")
    public ResponseEntity<SuccessResponse> updateWriterNameDescription(
            @PathVariable("writerNameId") final Long writerNameId,
            @RequestBody @Valid final WriterNameDescriptionUpdateRequest request,
            @UserId final Long userId
    ) {
        writerNameService.updateWriterNameDescription(userId, writerNameId, request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.WRITER_NAME_DESCRIPTION_UPDATE_SUCCESS));
    }
}
