package com.lexware.demo.infra.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RequestExceptionHandler {

    /*
     * Mapping of Spring exception types to http statuses
     */
    private static final Map<Class<? extends Throwable>, HttpStatus> HTTP_STATUS_BY_EXCEPTION_TYPES = getHttpStatuses();
    private static final Logger LOG = LoggerFactory.getLogger(RequestExceptionHandler.class);

    public RequestExceptionHandler() {
        //nothing
    }

    private static Map<Class<? extends Throwable>, HttpStatus> getHttpStatuses() {
        Map<Class<? extends Throwable>, HttpStatus> coll = new HashMap<>();
        coll.put(HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED);
        coll.put(HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        coll.put(HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE);
        coll.put(MissingPathVariableException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        coll.put(MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST);
        coll.put(ServletRequestBindingException.class, HttpStatus.BAD_REQUEST);
        coll.put(ConversionNotSupportedException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        coll.put(TypeMismatchException.class, HttpStatus.BAD_REQUEST);
        coll.put(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST);
        coll.put(HttpMessageNotWritableException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        coll.put(MissingServletRequestPartException.class, HttpStatus.BAD_REQUEST);
        coll.put(BindException.class, HttpStatus.BAD_REQUEST);
        coll.put(NoHandlerFoundException.class, HttpStatus.NOT_FOUND);
        coll.put(AsyncRequestTimeoutException.class, HttpStatus.SERVICE_UNAVAILABLE);
        coll.put(AccessDeniedException.class, HttpStatus.FORBIDDEN);

        return coll;
    }

    private static HttpStatus httpStatusOf(final Throwable throwable) {
        return HTTP_STATUS_BY_EXCEPTION_TYPES.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isInstance(throwable))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResult> handleBaseException(final HttpServletRequest request,
                                                           final HttpServletResponse response,
                                                           final BaseException e) {
        LOG.error(ExceptionUtils.getStackTrace(e));

        if (response.isCommitted()) {
            return null;
        }

        return ErrorResult.from(request, e).toResponseEntity();
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResult> handleThrowable(final HttpServletRequest request,
                                                       final HttpServletResponse response,
                                                       final Throwable e) {
        BaseException be = getBaseException(e);

        if (be != null) {
            return handleBaseException(request, response, be);
        }

        LOG.error(ExceptionUtils.getStackTrace(e));

        if (response.isCommitted()) {
            return null;
        }

        final HttpStatus status = httpStatusOf(e);

        return ErrorResult.from(request, new UnexpectedApplicationError(e, status)).toResponseEntity();
    }

    private BaseException getBaseException(final Throwable throwable) {
        BaseException be = null;
        if (throwable != null) {
            if (throwable instanceof BaseException ex) {
                return ex;
            } else if (throwable.getCause() != null) {
                be = getBaseException(throwable.getCause());
            }
        }
        return be;
    }
}
