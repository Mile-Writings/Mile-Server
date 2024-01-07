package com.mile.controller.curious;

import com.mile.curious.serivce.CuriousService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/curious")
@RequiredArgsConstructor
public class CuriousController implements CuriousControllerSwagger{

    private final CuriousService curiousService;

    @PostMapping("/{postId}")
    // @Override
    public void postCurious(@PathVariable final Long postId, final Principal principal) {
        curiousService.createCurious(postId, Long.valueOf(principal.getName()));
    }
}
