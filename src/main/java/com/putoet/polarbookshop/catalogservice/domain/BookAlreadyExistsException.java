package com.putoet.polarbookshop.catalogservice.domain;

import lombok.Getter;

public class BookAlreadyExistsException extends RuntimeException {
    @Getter
    private final Book book;

    public BookAlreadyExistsException(Book book) {
        super("A book with ISBN " + book.getIsbn() + " already exists.");
        this.book = book;
    }
}
