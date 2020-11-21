package com.putoet.polarbookshop.catalogservice.web;

import com.putoet.api.ApiError;
import com.putoet.api.ApiErrorResponse;
import com.putoet.polarbookshop.catalogservice.domain.BookAlreadyExistsException;
import com.putoet.polarbookshop.catalogservice.domain.BookNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class BookControllerAdvice {
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse bookNotFoundHandler(BookNotFoundException exc) {
        return ApiErrorResponse.of(new ApiError(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", exc));
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiErrorResponse bookAlreadyExistsHandler(BookAlreadyExistsException exc) {
        return ApiErrorResponse.of(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "BOOK_ALREADY_EXISTS", exc));
    }
}
