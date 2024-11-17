package com.mile.slack.module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class SendErrorModule extends SendWebhookMessage {
    private final StringBuilder sb = new StringBuilder();

    @Value("${webhook.url-for-error}")
    private String webHookUri;

    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    public void sendError(@NonNull final Exception exception) {
        WebClient webClient = WebClient.builder()
                .baseUrl(webHookUri).build();


        webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(generateMessage(exception))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.error("WEB HOOK 전송 중 에러 발생 -> {}", e.getMessage()))
                .subscribe();
    }


    private String readRootStackTrace(Exception error) {
        return error.getStackTrace()[0].toString();
    }

    private SendErrorModule.SlackMessage generateMessage(final Exception exception) {
        sb.append("🚨 ERROR").append("\n").append("\n").append(exception.toString()).append("\n");
        sb.append("PROFILE").append("\n").append("\n").append(profile).append("\n");
        sb.append("REQUEST ID").append("\n").append("\n").append(MDC.get("request_id")).append("\n");
        sb.append("DETAILS").append("\n").append("\n").append(readRootStackTrace(exception)).append("\n");

        return new SendErrorModule.SlackMessage(sb.toString());
    }

    @Getter
    @AllArgsConstructor
    private class SlackMessage {
        private String text;
    }
}
