package sample.spring.book.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sample.spring.book.server.Book;

public interface JpaBookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByAuthorStartingWith(String prefix);
}
