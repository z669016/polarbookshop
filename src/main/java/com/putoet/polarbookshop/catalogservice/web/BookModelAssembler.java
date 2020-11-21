package com.putoet.polarbookshop.catalogservice.web;

import com.putoet.polarbookshop.catalogservice.domain.Book;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<Book, EntityModel<Book>> {
    @Override
    public EntityModel<Book> toModel(Book book) {
        return EntityModel.of(book,
                linkTo(methodOn(BookController.class).getByIsbn(book.getIsbn())).withSelfRel(),
                linkTo(methodOn(BookController.class).get()).withRel("books"));
    }

    @Override
    public CollectionModel<EntityModel<Book>> toCollectionModel(Iterable<? extends Book> books) {
        final List<EntityModel<Book>> list = new ArrayList<>();
        books.iterator().forEachRemaining(book -> list.add(toModel(book)));
        return CollectionModel.of(list, linkTo(methodOn(BookController.class).get()).withSelfRel());
    }
}
