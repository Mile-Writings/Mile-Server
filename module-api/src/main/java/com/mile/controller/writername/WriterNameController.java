package com.mile.controller.writername;

import com.mile.authentication.PrincipalHandler;
import com.mile.dto.SuccessResponse;
import com.mile.exception.message.SuccessMessage;
import com.mile.writername.service.WriterNameDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/writerName")
public class WriterNameController implements WriterNameControllerSwagger{

    private final WriterNameDeleteService writerNameDeleteService;
    private final PrincipalHandler principalHandler;

    @Override
    @DeleteMapping("/{writerNameId}")
    public ResponseEntity<SuccessResponse> deleteMember(
            @PathVariable("writerNameId") final Long writerNameId
    ) {
        writerNameDeleteService.deleteWriterNameById(writerNameId, principalHandler.getUserIdFromPrincipal());
        return ResponseEntity.ok(SuccessResponse.of(SuccessMessage.MOIM_MEMBER_DELETE_SUCCESS));
    }
}
