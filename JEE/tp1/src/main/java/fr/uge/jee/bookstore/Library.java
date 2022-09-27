package fr.uge.jee.bookstore;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Library {
    private final Set<Book> books;

    public Library(Set<Book> books) {
        this.books = Objects.requireNonNull(books);
    };

    public String toString() {
        return books.stream().map(Object::toString).collect(Collectors.joining(","));
    }
 }
