package com.putoet.polarbookshop.catalogservice.persistence;

import com.putoet.polarbookshop.catalogservice.domain.Book;
import com.putoet.polarbookshop.catalogservice.domain.BookRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private static final Map<String, Book> books = new ConcurrentHashMap<>();

    static {
        Book book = new Book("1234567891", "Northern Lights", "Lyra Silvertongue", Year.of(2001));
        books.put(book.getIsbn(), book);
    }

    @Override
    public Collection<Book> findAllOrderByTitle() {
        return books.values().stream().sorted(Comparator.comparing(Book::getTitle)).collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return books.containsKey(isbn);
    }

    @Override
    public Book save(Book book) {
        return books.put(book.getIsbn(), book);
    }

    @Override
    public Book delete(Book book) {
        return books.remove(book.getIsbn());
    }

    @Override
    public Book update(String isbn, Book book) {
        return books.put(isbn, book);
    }
}
