package dev.azdanov.catalogservice.web.exceptions;

import dev.azdanov.catalogservice.domain.ProductNotFoundException;
import java.net.URI;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String SERVICE_NAME = "catalog-service";
    public static final String CATEGORY_GENERIC = "Generic";
    private static final URI INTERNAL_SERVER_ERROR_TYPE = URI.create("https://http.dev/500");
    private static final URI NOT_FOUND_TYPE = URI.create("https://http.dev/404");

    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnhandledException(Exception e) {
        log.error("Unhandled exception", e);

        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                INTERNAL_SERVER_ERROR_TYPE,
                e.getMessage(),
                CATEGORY_GENERIC);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    ProblemDetail handleProductNotFoundException(ProductNotFoundException e) {
        log.warn("Product not found", e);

        return buildProblemDetail(
                HttpStatus.NOT_FOUND, "Product Not Found", NOT_FOUND_TYPE, e.getMessage(), CATEGORY_GENERIC);
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
