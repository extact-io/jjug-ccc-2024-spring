package sample.microprofile.book.server.repository;

import java.util.List;
import java.util.Optional;

import sample.microprofile.book.server.Book;
import sample.microprofile.book.server.exception.DuplicateException;
import sample.microprofile.book.server.exception.NotFoundException;

public interface BookRepository {

    Optional<Book> get(int id);

    List<Book> findByAuthorStartingWith(String prefix);

    Book save(Book entty) throws DuplicateException, NotFoundException;

    void remove(int id) throws NotFoundException;
}
