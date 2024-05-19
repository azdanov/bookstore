package dev.azdanov.orderservice.web.exceptions;

import dev.azdanov.orderservice.domain.InvalidOrderException;
import dev.azdanov.orderservice.domain.OrderNotFoundException;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String SERVICE_NAME = "order-service";
    private static final String CATEGORY_GENERIC = "Generic";

    private static final URI INTERNAL_SERVER_ERROR_TYPE = URI.create("https://http.dev/500");
    private static final URI NOT_FOUND_TYPE = URI.create("https://http.dev/404");
    private static final URI BAD_REQUEST_TYPE = URI.create("https://http.dev/400");

    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnhandledException(Exception e) {
        log.error("Unhandled exception occurred");

        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                INTERNAL_SERVER_ERROR_TYPE,
                e.getMessage(),
                CATEGORY_GENERIC);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    ProblemDetail handleOrderNotFoundException(OrderNotFoundException e) {
        log.warn("Order not found");

        return buildProblemDetail(
                HttpStatus.NOT_FOUND, "Order Not Found", NOT_FOUND_TYPE, e.getMessage(), CATEGORY_GENERIC);
    }

    @ExceptionHandler(InvalidOrderException.class)
    ProblemDetail handleInvalidOrderException(InvalidOrderException e) {
        log.warn("Invalid order creation request");

        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid Order Creation Request",
                BAD_REQUEST_TYPE,
                e.getMessage(),
                CATEGORY_GENERIC);
    }

    @Override
    @Nullable protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList();

        log.warn("Invalid request payload: {}", errors);

        ProblemDetail problemDetail = buildProblemDetail(
                HttpStatus.BAD_REQUEST, "Bad Request", BAD_REQUEST_TYPE, "Invalid request payload", CATEGORY_GENERIC);
        problemDetail.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    private ProblemDetail buildProblemDetail(
            HttpStatus status, String title, URI type, String detail, String errorCategory) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(type);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", errorCategory);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
