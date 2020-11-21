package com.putoet.polarbookshop.catalogservice.web;

import com.putoet.polarbookshop.catalogservice.domain.Book;
import com.putoet.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.putoet.polarbookshop.catalogservice.domain.BookService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class BookController {
    private final BookService bookService;
    private final BookModelAssembler bookModelAssembler;

    public BookController(BookService bookService, BookModelAssembler bookModelAssembler) {
        this.bookService = bookService;
        this.bookModelAssembler = bookModelAssembler;
    }

    @GetMapping(value = BookResources.BOOKS, produces = { "application/hal+json" })
    public CollectionModel<EntityModel<Book>> get() {
        return bookModelAssembler.toCollectionModel(bookService.viewBookList());
    }

    @GetMapping(value = BookResources.BOOK_BY_ISBN, produces = { "application/hal+json" })
    public EntityModel<Book> getByIsbn(@PathVariable String isbn) {
        final Book book = bookService.viewBookDetails(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
        return bookModelAssembler.toModel(book);
    }

    @PostMapping(value = BookResources.BOOKS, produces = { "application/hal+json" })
    public EntityModel<Book> post(@RequestBody @Valid Book book) {
        return bookModelAssembler.toModel(bookService.addBookToCatalog(book));
    }

    @DeleteMapping(value = BookResources.BOOK_BY_ISBN, produces = { "application/hal+json" })
    public EntityModel<Book> delete(@PathVariable String isbn) {
        return bookModelAssembler.toModel(bookService.removeBookFromCatalog(isbn));
    }

    @PutMapping(value = BookResources.BOOK_BY_ISBN, produces = { "application/hal+json" })
    public EntityModel<Book> put(@PathVariable String isbn, @RequestBody @Valid Book book) {
        return bookModelAssembler.toModel(bookService.editBookDetails(isbn, book));
    }
}
