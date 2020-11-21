package com.putoet.polarbookshop.catalogservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.putoet.api.ApiError;
import com.putoet.api.ApiErrorResponse;
import com.putoet.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.URI;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.useDefaultDateFormatsOnly;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private Traverson getTraversonClient(String endpoint) {
        final String url = "http://localhost:" + this.port + endpoint;
        return new Traverson(URI.create(url), MediaTypes.HAL_JSON);
    }

    @Test
    void whenPostRequestThenBookCreated() {
        final Book expectedBook = new Book("1234567890", "Title", "Author", Year.of(2020));
        final ResponseEntity<Book> response = restTemplate.postForEntity(BookResources.BOOKS, expectedBook, Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEqualTo(expectedBook);
    }

    @Test
    void whenBadPostRequestThenBadRequest() {
        final Book expectedBook = new Book("1234567890", "Title", "", Year.of(2020));
        final ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(BookResources.BOOKS, expectedBook, ApiErrorResponse.class);
        System.out.println(response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenGetRequestThenOkForEntityModel() {
        final ResponseEntity<String> list = restTemplate.getForEntity(BookResources.BOOKS, String.class);
        System.out.println("list:" + list);

        final ParameterizedTypeReference<CollectionModel<EntityModel<Book>>> booksCollection =
                new ParameterizedTypeReference<CollectionModel<EntityModel<Book>>>() {};

        final ResponseEntity<CollectionModel<EntityModel<Book>>> response = restTemplate.exchange(BookResources.BOOKS,
                HttpMethod.GET,
                null,
                booksCollection,
                Collections.emptyMap());

        System.out.println("response:" + response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Beware: in order for this to work, Book required a NoArgs constructor and
        // an alternative setter for the publishing year property
        final Traverson traverson = getTraversonClient(BookResources.BOOKS);
        final CollectionModel<EntityModel<Book>> bookResources =
                traverson.follow().toObject(booksCollection);
        System.out.println(bookResources);

        System.out.println("books: " + bookResources.getContent().stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList()));
    }

    @Test
    void whenGetForIsbnRequestThenOk() {
        final String isbn = "1234567891";
        final ResponseEntity<Book> response = restTemplate.getForEntity(BookResources.BOOK_BY_ISBN, Book.class, isbn);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final Book expectedBook = response.getBody();
        assertThat(expectedBook.getIsbn()).isEqualTo(isbn);
    }


    @Test
    void whenGetForIsbnRequestThenNotOk() {
        final String isbn = "1234567890";
        final ParameterizedTypeReference<EntityModel<Book>> bookType =
                new ParameterizedTypeReference<EntityModel<Book>>() {};

        final Traverson traverson = getTraversonClient(BookResources.BOOKS + "/" + isbn);
//        final ApiError error =
//                traverson.follow().toObject(ApiError.API_ERROR_TYPE);

        final TraversonRequest<EntityModel<Book>> bookModel = TraversonRequest.apply(traverson.follow(), bookType);
        if (bookModel.isError()) {
            System.out.println(bookModel.error());
            System.out.println(bookModel.error().getMessage());
        } else
            System.out.println(bookModel.get());
    }


    public static class TraversonRequest<T> {
        private Traverson.TraversalBuilder builder;

        private T object = null;
        private HttpClientErrorException exc;

        private TraversonRequest(Traverson.TraversalBuilder builder) {
            this.builder = builder;
        }

        public TraversonRequest<T> apply(ParameterizedTypeReference<T> ref) {
            try {
                object = builder.toObject(ref);
            } catch (HttpClientErrorException exc) {
                this.exc = exc;
            }

            return this;
        }

        public boolean isPresent() {
            return object != null;
        }

        public T get() {
            return object;
        }

        public boolean isError() {
            return exc != null;
        }

        public ApiError error() {
            final ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(exc.getResponseBodyAsByteArray(), ApiErrorResponse.class).getEpiError();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            throw exc;
        }

        public static <T> TraversonRequest<T> apply(Traverson.TraversalBuilder builder, ParameterizedTypeReference<T> ref) {
            return new TraversonRequest(builder).apply(ref);
        }
    }
}