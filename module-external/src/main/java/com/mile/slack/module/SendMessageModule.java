package com.mile.slack.module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Component
@Slf4j
public class SendMessageModule implements SendWebhookMessage {
    private final StringBuilder sb = new StringBuilder();

    @Value("${webhook.url-for-event}")
    private String webHookUri;

    @Override
    public void sendMessage(@NonNull final String message) {
        WebClient webClient = WebClient.builder()
                .baseUrl(webHookUri).build();


        webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(generateMessage(message))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.error("WEB HOOK 전송 중 에러 발생 -> {}", e.getMessage()))
                .subscribe();
    }

    private SlackMessage generateMessage(final String message) {
        sb.append("*[인기글 삭제 작업 완료]*").append("\n").append(message).append("\n");
        sb.append("*[진행일자]*").append("\n").append(LocalDateTime.now()).append("\n");

        return new SlackMessage(sb.toString());
    }

    @Getter
    @AllArgsConstructor
    private class SlackMessage {
        private String text;
    }
}
