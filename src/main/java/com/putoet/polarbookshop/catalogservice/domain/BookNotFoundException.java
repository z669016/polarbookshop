package com.putoet.polarbookshop.catalogservice.domain;

import lombok.Getter;

public class BookNotFoundException extends RuntimeException {
    @Getter
    private final String isbn;

    public BookNotFoundException(String isbn) {
        super("A book with ISBN " + isbn + " was not found.");
        this.isbn = isbn;
    }
}
