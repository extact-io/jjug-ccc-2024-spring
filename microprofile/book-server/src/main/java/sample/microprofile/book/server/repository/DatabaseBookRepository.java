package sample.microprofile.book.server.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sample.microprofile.book.server.Book;
import sample.microprofile.book.server.exception.DuplicateException;
import sample.microprofile.book.server.exception.NotFoundException;

//@ApplicationScoped
@Transactional
public class DatabaseBookRepository implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> get(int id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> findByAuthorStartingWith(String prefix) {
        String jpql = "select b from Book b where b.author LIKE ?1 order by b.id";
        return em.createQuery(jpql, Book.class)
                .setParameter(1, prefix + "%")
                .getResultList();
    }

    @Override
    public Book save(Book book) throws DuplicateException, NotFoundException {

        if (book.getId() != null) {
            get(book.getId()).orElseThrow(() -> new NotFoundException("id:" + book.getId()));
        }

        Book saved = em.merge(book);
        em.flush();

        return saved;
    }

    @Override
    public void remove(int id) throws NotFoundException {
        Book book = get(id).orElseThrow(() -> new NotFoundException("id:" + id));
        em.remove(book);
        em.flush();
    }
}
