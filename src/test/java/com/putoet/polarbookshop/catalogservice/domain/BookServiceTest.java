package com.putoet.polarbookshop.catalogservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.Year;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookServiceTest {
    private static final List<Book> BOOKS = List.of(
            new Book("0123456789", "BOOK-1", "AUTHOR-1", Year.of(2020)),
            new Book("1123456789", "BOOK-2", "AUTHOR-2", Year.of(2019))
            );

    private static final Book NEW_BOOK = new Book("2123456789", "BOOK-3", "AUTHOR-3", Year.of(2018));
    private static final String NON_EXISTING_ISBN = "0000000000";

    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setup() {
        bookRepository = mock(BookRepository.class);
        for (int idx = 0; idx < BOOKS.size(); idx++) {
            when(bookRepository.existsByIsbn(BOOKS.get(idx).getIsbn())).thenReturn(true);
            when(bookRepository.findByIsbn(BOOKS.get(idx).getIsbn())).thenReturn(Optional.of(BOOKS.get(idx)));
            when(bookRepository.delete(BOOKS.get(idx))).thenReturn(BOOKS.get(idx));
        }

        when(bookRepository.existsByIsbn(NON_EXISTING_ISBN)).thenReturn(false);
        when(bookRepository.findByIsbn(NON_EXISTING_ISBN)).thenReturn(Optional.empty());
        when(bookRepository.findAllOrderByTitle()).thenReturn(BOOKS);

        bookService = new BookService(bookRepository);
    }

    @Test
    void viewBookList() {
        final Collection<Book> books = bookService.viewBookList();
        assertThat(books.size()).isEqualTo(2);

        final List<String> titles = books.stream().map(Book::getTitle).collect(Collectors.toList());
        for (int idx = 0; idx < titles.size(); idx++)
            assertThat(titles.get(idx)).isEqualTo(BOOKS.get(idx).getTitle());
    }

    @Test
    void viewBookDetails() {
        final Book book = BOOKS.get(1);
        final Optional<Book> found = bookService.viewBookDetails(book.getIsbn());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    void viewBookDetailsNotFound() {
        final Optional<Book> notFound = bookService.viewBookDetails(NON_EXISTING_ISBN);

        assertThat(notFound.isEmpty()).isTrue();
    }

    @Test
    void addBookToCatalog() {
        final Book added = bookService.addBookToCatalog(NEW_BOOK);
        assertThat(added.getIsbn()).isEqualTo(NEW_BOOK.getIsbn());
    }

    @Test
    void addBookToCatalogExistingBook() {
        final Book book = BOOKS.get(0);
        assertThrows(BookAlreadyExistsException.class, () -> bookService.addBookToCatalog(book));
    }

    @Test
    void removeBookFromCatalog() {
        final Book book = BOOKS.get(0);
        final Book deleted = bookService.removeBookFromCatalog(book.getIsbn());

        assertThat(deleted.getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    void removeBookFromCatalogNotExisting() {
        assertThrows(BookNotFoundException.class, () -> bookService.removeBookFromCatalog(NON_EXISTING_ISBN));
    }

    @Test
    void editBookDetails() {
        final Book book = BOOKS.get(0);
        book.setTitle("BLABLA");

        assertThat(bookService.editBookDetails(book.getIsbn(), book).getTitle()).isEqualTo("BLABLA");
    }

    @Test
    void editBookDetailsNotExisting() {
        assertThrows(BookNotFoundException.class, () -> bookService.editBookDetails(NEW_BOOK.getIsbn(), NEW_BOOK));
    }
}