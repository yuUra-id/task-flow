package com.sharko.yura.taskflow.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Глобальный обработчик исключений приложения.
 * Перехватывает все пользовательские и системные исключения,
 * возникающие в контроллерах, и преобразует их в единый
 * структурированный JSON-ответ.
 * Позволяет:
 * Возвращать корректные HTTP-статусы.
 * Формировать единый формат ошибки.
 * Обрабатывать ошибки валидации DTO
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Если пользователь не найден
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(
            UserNotFoundException exception, HttpServletRequest request) {

        return buildErrorResponse(exception, request, HttpStatus.NOT_FOUND);

    }
    //Несоответствия паролей
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handleException(
            PasswordMismatchException exception, HttpServletRequest request) {

        return buildErrorResponse(exception, request, HttpStatus.BAD_REQUEST);

    }
    //Обработка, если пользователь с username уже существует
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(
            UserAlreadyExistsException exception, HttpServletRequest request) {

        return buildErrorResponse(exception, request, HttpStatus.CONFLICT);

    }
    //Обработка, если пользователь с email уже существует
    @ExceptionHandler(UserWithEmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(
            UserWithEmailAlreadyExistsException exception, HttpServletRequest request) {

        return buildErrorResponse(exception, request, HttpStatus.CONFLICT);

    }
    //Обработка ошибок валидации DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(
            MethodArgumentNotValidException exception, HttpServletRequest request) {

        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage("Validation error");
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setErrors(errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }
    //Формирует стандартный ответ об ошибке
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            RuntimeException exception, HttpServletRequest request, HttpStatus status) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setErrors(List.of(exception.getMessage()));

        return new ResponseEntity<>(errorResponse, status);

    }

}
