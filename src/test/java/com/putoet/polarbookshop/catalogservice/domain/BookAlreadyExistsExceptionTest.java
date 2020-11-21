package com.putoet.polarbookshop.catalogservice.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BookAlreadyExistsExceptionTest {

    @Test
    void getBook() {
        final BookAlreadyExistsException exc = new BookAlreadyExistsException(BookTest.BOOK);
        assertThat(exc.getBook()).isEqualTo(BookTest.BOOK);
    }
}