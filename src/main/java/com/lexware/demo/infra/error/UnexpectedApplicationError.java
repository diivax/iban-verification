package com.lexware.demo.infra.error;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class UnexpectedApplicationError extends BaseException {
    public static final UUID ERROR_ID = UUID.fromString("529629ba-f5cb-445f-bf07-2d971c7a89d8");
    public static final String ERROR_NAME = "UNEXPECTED_APPLICATION_ERROR";

    public UnexpectedApplicationError(final HttpStatus httpStatus,
                                      final String message,
                                      final Throwable cause,
                                      final Object details) {
        super(ERROR_ID,
                ERROR_NAME,
                Optional.ofNullable(httpStatus).orElse(HttpStatus.INTERNAL_SERVER_ERROR),
                message,
                cause,
                details
        );
    }

    public UnexpectedApplicationError(final Throwable cause, final HttpStatus httpStatus) {
        this(httpStatus, message(cause), cause, details(cause));
    }

    private static String message(final Throwable cause) {
        return ExceptionUtils.getMessage(cause);
    }

    private static Object details(final Throwable cause) {
        if (cause == null) {
            return null;
        }

        return new HashMap<String, String>().put("causedBy", message(cause));
    }
}