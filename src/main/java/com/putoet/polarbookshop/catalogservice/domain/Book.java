package com.putoet.polarbookshop.catalogservice.domain;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.Year;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @NotBlank(message="BOOK_ISBN_NONBLANK")
    @Pattern(regexp = "^(97([89]))?\\d{9}(\\d|X)$", message = "BOOK_ISBN_INVALID")
    private String isbn;

    @NotBlank(message="BOOK_TITLE_NONBLANK")
    private String title;

    @NotBlank(message="BOOK_AUTHOR_NONBLANK")
    private String author;

    @NotNull(message="BOOK_PUB_YEAR_NOTNULL")
    @PastOrPresent(message="BOOK_PUB_YEAR_PAST_PRESENT")
    private Year publishingYear;

    @JsonSetter("publishingYear")
    public void setPublisingYear(int year) {
        this.publishingYear = Year.of(year);
    }
}
