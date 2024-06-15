package sample.microprofile.book.server;

import java.util.List;
import java.util.Optional;

import sample.microprofile.book.server.exception.DuplicateException;
import sample.microprofile.book.server.exception.NotFoundException;
import sample.microprofile.book.server.interceptor.TraceLoggable;

@TraceLoggable
public interface BookRepository {

    Optional<Book> get(int id);

    List<Book> findByAuthorStartingWith(String prefix);

    Book save(Book entty) throws DuplicateException, NotFoundException;

    void remove(int id) throws NotFoundException;
}
