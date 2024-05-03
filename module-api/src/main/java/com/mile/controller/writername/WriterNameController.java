package com.mile.controller.writername;

import com.mile.authentication.PrincipalHandler;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.writername.service.WriterNameDeleteService;
import com.mile.writername.service.WriterNameService;
import com.mile.writername.service.dto.WriterNameDescriptionResponse;
import com.mile.writername.service.dto.WriterNameDescriptionUpdateRequest;
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
@RequestMapping("/api/writerName")
public class WriterNameController implements WriterNameControllerSwagger {

    private final WriterNameDeleteService writerNameDeleteService;
    private final WriterNameService writerNameService;
    private final PrincipalHandler principalHandler;

    @Override
    @DeleteMapping("/{writerNameId}")
    public ResponseEntity<SuccessResponse> deleteMember(
            @PathVariable("writerNameId") final Long writerNameId
    ) {
        writerNameDeleteService.deleteWriterNameById(writerNameId, principalHandler.getUserIdFromPrincipal());
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_MEMBER_DELETE_SUCCESS));
    }


    @Override
    @GetMapping("/{writerNameId}/profile")
    public ResponseEntity<SuccessResponse<WriterNameDescriptionResponse>> getWriterNameDescription(
            @PathVariable("writerNameId") final Long writerNameId
    ) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.WRITER_NAME_GET_SUCCESS, writerNameService.findWriterNameDescription(principalHandler.getUserIdFromPrincipal(), writerNameId)));
    }

    @Override
    @PatchMapping("/{writerNameId}/description")
    public ResponseEntity<SuccessResponse> updateWriterNameDescription(
            @PathVariable("writerNameId") final Long writerNameId,
            @RequestBody final WriterNameDescriptionUpdateRequest request
    ) {
        writerNameService.updateWriterNameDescription(principalHandler.getUserIdFromPrincipal(), writerNameId, request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.WRITER_NAME_DESCRIPTION_UPDATE_SUCCESS));
    }
}
