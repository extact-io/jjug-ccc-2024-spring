package sample.microprofile.book.client;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BookService {

    private BookClient bookClient;

    @Inject
    public BookService(@RestClient BookClient bookClient) {
        this.bookClient = bookClient;
    }

    public BookDto get(int id) {
        return bookClient.get(id);
    }

    public List<BookDto> findByAuthorStartingWith(String prefix) {
        return bookClient.findByAuthorStartingWith(prefix);
    }

    public BookDto add(BookDto book) {
        return bookClient.add(book);
    }

    public BookDto update(BookDto book) {
        return bookClient.update(book);
    }

    public void delete(int id) {
        bookClient.delete(id);
    }
}
