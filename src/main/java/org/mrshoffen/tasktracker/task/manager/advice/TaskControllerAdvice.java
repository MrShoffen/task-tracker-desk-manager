package org.mrshoffen.tasktracker.task.manager.advice;


import lombok.extern.slf4j.Slf4j;
import org.mrshoffen.tasktracker.task.manager.exception.TaskNotFoundException;
import org.mrshoffen.tasktracker.task.manager.exception.TaskStructureException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class TaskControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errors = e.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(" | "));
        var problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, errors);
        problemDetail.setTitle("Bad Request");
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Некорректный формат UUID для задачи-родителя");
        problemDetail.setTitle("Bad Request");
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(problemDetail);
    }

//    @Override
//    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        var problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Некорректный id задачи");
//        problemDetail.setTitle("Bad Request");
//        return ResponseEntity
//                .status(BAD_REQUEST)
//                .body(problemDetail);
//    }

    @Nullable
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Некорректный id задачи");
        problemDetail.setTitle("Bad Request");
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserAlreadyExistException(TaskNotFoundException e) {
        ProblemDetail problem = generateProblemDetail(NOT_FOUND, e);
        return ResponseEntity.status(NOT_FOUND).body(problem);
    }

    @ExceptionHandler(TaskStructureException.class)
    public ResponseEntity<ProblemDetail> handleTaskStructureException(TaskStructureException e) {
        ProblemDetail problem = generateProblemDetail(BAD_REQUEST, e);
        return ResponseEntity.status(BAD_REQUEST).body(problem);
    }

    private ProblemDetail generateProblemDetail(HttpStatus status, Exception ex) {
        log.warn("Error occured: {}", ex.getMessage());

        var problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setTitle(status.getReasonPhrase());
        return problemDetail;
    }

}
