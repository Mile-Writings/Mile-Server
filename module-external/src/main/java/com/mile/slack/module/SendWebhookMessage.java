package com.mile.slack.module;

import org.springframework.lang.NonNull;

public interface SendWebhookMessage {
    void sendMessage(@NonNull final String message);

}
