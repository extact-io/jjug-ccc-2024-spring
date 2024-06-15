package sample.spring.book.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import sample.spring.book.server.Book;
import sample.spring.book.server.BookRepository;
import sample.spring.book.server.exception.DuplicateException;
import sample.spring.book.server.exception.NotFoundException;

@Repository
@ConditionalOnProperty(name = "use.repository", havingValue = "jpa", matchIfMissing = true)
@Transactional
public class JpaBookRepositoryAdapter implements BookRepository {

    private JpaBookRepository repository;

    public JpaBookRepositoryAdapter(JpaBookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Book> get(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Book> findByAuthorStartingWith(String prefix) {
        return repository.findByAuthorStartingWith(prefix);
    }

    @Override
    public Book save(Book book) throws DuplicateException, NotFoundException {
        if (book.getId() != null) {
            this.get(book.getId()).orElseThrow(() -> new NotFoundException("id:" + book.getId()));
        }
        return repository.saveAndFlush(book);
    }

    @Override
    public void remove(int id) throws NotFoundException {
        Book book = get(id).orElseThrow(() -> new NotFoundException("id:" + id));
        repository.delete(book);
        repository.flush();
    }
}
