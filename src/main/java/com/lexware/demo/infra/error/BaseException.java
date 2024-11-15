package com.lexware.demo.infra.error;

import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class BaseException extends RuntimeException {
    public final UUID errorId;
    public final String errorName;
    public final HttpStatus httpStatus;

    private final transient Optional<Object> details;

    protected BaseException(final UUID errorId,
                            final String errorName,
                            final HttpStatus httpStatus,
                            final String message,
                            final Throwable cause,
                            final Object details) {
        super(validateArg(message, "message"), cause);

        this.errorId = Objects.requireNonNull(errorId, "'errorId' must not be null!");
        this.errorName = validateArg(errorName, "errorName");
        this.httpStatus = Objects.requireNonNull(httpStatus, "'httpStatus' must not be null!");
        this.details = Optional.ofNullable(details);
    }

    protected static String validateArg(String arg, String argName) {
        if (StringUtils.isBlank(arg)) {
            throw new IllegalArgumentException(String.format("'%s' must not be null or empty!", argName));
        }
        return arg;
    }

    public final Optional<Object> details() {
        return details;
    }
}
