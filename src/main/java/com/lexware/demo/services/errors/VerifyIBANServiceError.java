package com.lexware.demo.services.errors;

import com.lexware.demo.infra.error.BaseException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class VerifyIBANServiceError extends BaseException {
    public static final UUID ERROR_ID = UUID.fromString("9c9861a4-3f4a-4217-9171-df734e3d3e70");
    public static final String ERROR_NAME = "VERIFY_IBAN_SERVICE_ERROR";

    public VerifyIBANServiceError(final String message,
                                  final Throwable cause,
                                  final Object details) {
        super(ERROR_ID,
                ERROR_NAME,
                HttpStatus.UNPROCESSABLE_ENTITY,
                message,
                cause,
                details
        );
    }

    public VerifyIBANServiceError(final String message) {
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
