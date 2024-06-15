package sample.spring.book.server;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sample.spring.book.server.exception.DuplicateException;
import sample.spring.book.server.exception.NotFoundException;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable int id) {
        return repository.get(id).orElse(null);
    }

    @GetMapping(path = "/author", params = "title")
    public List<Book> findByAuthorStartingWith(@RequestParam("title") String prefix) {
        return repository.findByAuthorStartingWith(prefix);
    }

    @PostMapping
    public Book add(@RequestBody Book book) throws DuplicateException {
        return repository.save(book);
    }

    @PutMapping
    public Book update(@RequestBody Book book) throws NotFoundException {
        var ret = repository.save(book);
        return ret;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) throws NotFoundException {
        repository.remove(id);
    }
}
