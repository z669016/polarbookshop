package com.putoet.polarbookshop.catalogservice.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BookNotFoundExceptionTest {

    @Test
    void getIsbn() {
        final BookNotFoundException exc = new BookNotFoundException(BookTest.BOOK.getIsbn());
        assertThat(exc.getIsbn()).isEqualTo(BookTest.BOOK.getIsbn());
    }
}