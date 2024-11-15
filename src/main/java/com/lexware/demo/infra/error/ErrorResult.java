package com.lexware.demo.infra.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class ErrorResult {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorResult.class);

    public final UUID errorId;
    public final String errorName;
    public final String message;
    public final String requestPath;
    public final OffsetDateTime timestamp;
    @JsonIgnore
    public final HttpStatus httpStatus;
    public final Object details;

    private ErrorResult(final Builder builder) {
        this.errorId = builder.errorId;
        this.errorName = builder.errorName;
        this.message = builder.message;
        this.requestPath = builder.requestPath;
        this.timestamp = builder.timestamp;
        this.httpStatus = builder.httpStatus;
        this.details = builder.details;
    }

    public static Builder builder(final HttpServletRequest request) {
        Objects.requireNonNull(request, "'request' must not be null!");
        final HttpStatus httpStatus = httpStatusOf(request);

        return new Builder(httpStatus);
    }

    public static ErrorResult from(final HttpServletRequest request, final BaseException ex) {
        Builder builder = builder(request);

        builder.withRequestAttributes(request)
                .withErrorId(ex.errorId)
                .withErrorName(ex.errorName)
                .withMessage(ex.getMessage())
                .withTimestamp()
                .withCustomHttpStatus(ex.httpStatus);

        return ex.details().map(builder::withDetails).orElse(builder).build();
    }

    private static HttpStatus httpStatusOf(final HttpServletRequest request) {
        final Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");

        if (statusCode == null) {
            LOG.warn("Http status code of servlet could not be retrieved!");
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        try {
            return HttpStatus.valueOf(statusCode);
        } catch (final Exception e) {
            LOG.error(String.format("Http status code of Servlet could not be parsed! " +
                            "Status code: %s, error message: %s ",
                    statusCode,
                    e.getMessage())
            );

            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public ResponseEntity<ErrorResult> toResponseEntity() {
        return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON).body(this);
    }

    public static class Builder {
        private UUID errorId;
        private String errorName;
        private String message;
        private String requestPath;
        private OffsetDateTime timestamp;
        private HttpStatus httpStatus;
        private Object details;

        private Builder(final HttpStatus httpStatus) {
            this.httpStatus = nonNull(httpStatus, "httpStatus");
        }

        private static <T> T nonNull(final T value, final String name) {
            return Objects.requireNonNull(value, String.format("'%s' must not be null!", name));
        }

        private static String nonBlank(final String value, final String name) {
            if (StringUtils.isBlank(value)) {
                throw new IllegalArgumentException(String.format("'%s' must not be blank!", name));
            }

            return value;
        }

        public Builder withErrorId(final UUID errorId) {
            this.errorId = nonNull(errorId, "errorId");
            return this;
        }

        public Builder withErrorName(final String errorName) {
            this.errorName = nonBlank(errorName, "errorName");
            return this;
        }

        public Builder withMessage(final String message) {
            this.message = nonBlank(message, "message");
            return this;
        }

        public Builder withRequestPath(final String requestPath) {
            this.requestPath = nonBlank(requestPath, "requestPath");
            return this;
        }

        public Builder withRequestAttributes(final HttpServletRequest request) {
            this.requestPath = nonNull(request, "request").getRequestURI();
            return this;
        }

        public Builder withTimestamp(final OffsetDateTime timestamp) {
            this.timestamp = nonNull(timestamp, "timestamp");
            return this;
        }

        public Builder withTimestamp() {
            return withTimestamp(OffsetDateTime.now());
        }

        public Builder withCustomHttpStatus(final HttpStatus httpStatus) {
            this.httpStatus = nonNull(httpStatus, "httpStatus");
            return this;
        }

        public Builder withDetails(final Object details) {
            this.details = nonNull(details, "details");
            return this;
        }

        public ErrorResult build() {
            return new ErrorResult(validate());
        }

        private Builder validate() {
            nonNull(errorId, "errorId");
            nonBlank(errorName, "errorName");
            nonBlank(message, "message");
            nonBlank(requestPath, "requestPath");
            nonNull(timestamp, "timestamp");

            return this;
        }

    }
}
