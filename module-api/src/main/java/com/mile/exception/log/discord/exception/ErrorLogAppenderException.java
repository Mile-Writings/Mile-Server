package com.mile.exception.log.discord.exception;

import com.mile.exception.message.ErrorMessage;
import com.mile.exception.model.MileException;

public class ErrorLogAppenderException extends MileException {
    public ErrorLogAppenderException() {
        super(ErrorMessage.DISCORD_LOG_APPENDER_ERROR);
    }
}
