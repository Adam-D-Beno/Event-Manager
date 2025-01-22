package org.das.event_manager.exeption;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidateException(MethodArgumentNotValidException e) {
        LOGGER.error("Handle Bad request exception: MethodArgumentNotValidException = {}", e.getBindingResult());
        String detailMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("%s : %s. Rejected value: %s",
                        error.getField(), error.getDefaultMessage(), error.getRejectedValue())
                ).collect(Collectors.joining(", "));

        var error = new ErrorMessageResponse(
                "Bad request",
                detailMessage,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidateException(ConstraintViolationException e) {
        LOGGER.error("Handle Bad request exception: ConstraintViolationException = {}", e.getConstraintViolations());
        String detailMessage = e.getConstraintViolations()
                .stream()
                .map(error -> String.format("%s %s %s Rejected value: %s",
                       error.getRootBeanClass().getSimpleName(), error.getPropertyPath(),
                       error.getMessage(), error.getInvalidValue())
                ).collect(Collectors.joining(", "));

        var error = new ErrorMessageResponse(
                "Bad request. Argument is NULL",
                detailMessage,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundException(EntityNotFoundException e) {
        LOGGER.error("Handle EntityNotFoundException = {}", e.getMessage());
        var error = new ErrorMessageResponse(
                "No such Element found",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.error("Handle Bad request exception: IllegalArgumentException = {}", e.getMessage());
        var error = new ErrorMessageResponse(
                "Bad request. Argument have error",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageResponse> HandlerHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        LOGGER.error("Handle Bad request exception: HttpMessageNotReadableException  = {}", e.getMessage());
        String detailMessage = "Body must not be is null";
        var error = new ErrorMessageResponse(
                "Bad request",
                detailMessage,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessageResponse> handleBadCredentialsException(BadCredentialsException e) {
        LOGGER.error("Handle BadCredentialsException = {}", e.getMessage());
        var error = new ErrorMessageResponse(
                "Failed to authenticate",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleGenericException(Exception e) {
        LOGGER.error("Handle generic exception = {}", e.getMessage());
        var error = new ErrorMessageResponse(
                "INTERNAL_SERVER_ERROR",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
