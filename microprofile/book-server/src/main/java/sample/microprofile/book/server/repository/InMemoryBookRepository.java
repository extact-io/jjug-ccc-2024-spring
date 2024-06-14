package sample.microprofile.book.server.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.annotation.PostConstruct;
import sample.microprofile.book.server.Book;
import sample.microprofile.book.server.exception.DuplicateException;
import sample.microprofile.book.server.exception.NotFoundException;

public class InMemoryBookRepository implements BookRepository {

    private Map<Integer, Book> bookMap;

    @PostConstruct
    public void init() {
        bookMap = new ConcurrentHashMap<>();
        bookMap.put(1, new Book(1, "燃えよ剣", "司馬遼太郎"));
        bookMap.put(2, new Book(2, "峠", "司馬遼太郎"));
        bookMap.put(3, new Book(3, "ノルウェーの森", "村上春樹"));
    }

    @Override
    public Book get(int id) {
        return bookMap.get(id);
    }

    @Override
    public List<Book> findByAuthorStartingWith(String prefix) {
        return bookMap.values().stream()
                .filter(book -> book.getAuthor().startsWith(prefix))
                .toList();
    }

    @Override
    public Book findByTitle(String title) {
        return bookMap.values().stream()
                .filter(book -> book.getTitle().equals(title))
                .findFirst()
                .get();
    }

    @Override
    public Book save(Book book) throws DuplicateException, NotFoundException {
        book = book.clone();
        if (book.getId() != null) { // for update
            if (findByTitle(book.getTitle()) != null) {
                throw new DuplicateException("title:" + book.getTitle());
            }
        } else { // for add
            int nextId = bookMap.keySet().stream().max(Integer::compareTo).get() + 1;
            book.setId(nextId);
        }
        return bookMap.put(book.getId(), book);
    }

    @Override
    public void remove(int id) throws NotFoundException {
        if (!bookMap.containsKey(id)) {
            throw new NotFoundException("id:" + id);
        }
        bookMap.remove(id);
    }

}
