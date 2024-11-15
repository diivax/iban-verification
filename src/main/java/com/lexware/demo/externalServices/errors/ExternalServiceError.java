package com.lexware.demo.externalServices.errors;

import com.lexware.demo.infra.error.BaseException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.UUID;

public class ExternalServiceError extends BaseException {
    public static final UUID ERROR_ID = UUID.fromString("9f0a7786-7121-4c32-8317-fd779fd57c7e");
    public static final String ERROR_NAME = "EXTERNAL_SERVICE_ERROR";

    public ExternalServiceError(final String message,
                                final Throwable cause,
                                final Object details) {
        super(ERROR_ID,
                ERROR_NAME,
                HttpStatus.BAD_GATEWAY,
                message,
                cause,
                details
        );
    }

    public ExternalServiceError(final String message) {
        this(message, null, null);
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
