package sample.spring.book.server.secure;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sample.spring.book.server.Book;
import sample.spring.book.server.BookRepository;
import sample.spring.book.server.exception.ExceptionMapping;

@RestController
@ExceptionMapping
@RequestMapping("/secure/books")
public class SecureBookController {

    private BookRepository repository;

    public SecureBookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/author")
    public List<Book> findByAuthorStartingWith(Authentication authentication, @RequestParam("title") String prefix) {
        return repository.findByAuthorStartingWith(authentication.getName()); // 自著を検索
    }
}
