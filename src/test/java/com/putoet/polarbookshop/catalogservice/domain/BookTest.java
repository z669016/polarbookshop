package com.putoet.polarbookshop.catalogservice.domain;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {
    public static final Book BOOK = new Book("1234567890", "Author", "Title", Year.of(2020));

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        final Set<ConstraintViolation<Book>> violations = validator.validate(BOOK);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenAnyFieldIsEmptyThenValidationFails() {
        final Book book = new Book("", "", "", null);
        final Set<ConstraintViolation<Book>> violations = validator.validate(book);

        final List<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        assertThat(messages).containsAll(List.of("BOOK_AUTHOR_NONBLANK", "BOOK_PUB_YEAR_NOTNULL", "BOOK_TITLE_NONBLANK", "BOOK_ISBN_NONBLANK"));
    }
}