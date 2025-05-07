package org.mrshoffen.tasktracker.desk.advice;


import lombok.extern.slf4j.Slf4j;
import org.mrshoffen.tasktracker.desk.exception.DeskStructureException;
import org.mrshoffen.tasktracker.desk.exception.DeskAlreadyExistsException;
import org.mrshoffen.tasktracker.desk.exception.DeskNotFoundException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class DeskControllerAdvice {


    @ExceptionHandler(DeskStructureException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleTaskStructureException(DeskStructureException e) {
        ProblemDetail problem = generateProblemDetail(NOT_FOUND, e);
        return Mono.just(ResponseEntity.status(NOT_FOUND).body(problem));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleValidationErrors(WebExchangeBindException e) {
        String errors = e.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(" | "));
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors);
        problemDetail.setTitle("Validation error");
        return Mono.just(ResponseEntity.badRequest().body(problemDetail));
    }

    @ExceptionHandler(DeskNotFoundException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleTaskNotFoundException(DeskNotFoundException e) {
        ProblemDetail problem = generateProblemDetail(NOT_FOUND, e);
        return Mono.just(ResponseEntity.status(NOT_FOUND).body(problem));
    }

    @ExceptionHandler(DeskAlreadyExistsException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleTaskAlreadyExistsException(DeskAlreadyExistsException e) {
        ProblemDetail problem = generateProblemDetail(CONFLICT, e);
        return Mono.just(ResponseEntity.status(CONFLICT).body(problem));
    }

    private ProblemDetail generateProblemDetail(HttpStatus status, Exception ex) {
        log.warn("Error occured: {}", ex.getMessage());

        var problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setTitle(status.getReasonPhrase());
        return problemDetail;
    }

}
