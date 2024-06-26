package sample.spring.book.server;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.main.banner-mode=off")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
abstract class AbstractBookControllerTest {

    private BookControllerClient target;

    private final Book expectedBook1 = new Book(1, "燃えよ剣", "司馬遼太郎");
    private final Book expectedBook2 = new Book(2, "峠", "司馬遼太郎");

    @BeforeEach
    void beforeEach(@Value("${local.server.port}") int port) {

        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:%s".formatted(port))
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        target = factory.createClient(BookControllerClient.class);
    }

    @Test
    @Order(1)
    void testGet() {

        Book actual = target.get(1);
        assertThat(actual).isEqualTo(expectedBook1);

        actual = target.get(999);
        assertThat(actual).isNull();
    }

    @Test
    @Order(1)
    void testFindByAuthorStartingWith() {

        List<Book> actual = target.findByAuthorStartingWith("司馬");
        assertThat(actual).containsExactly(expectedBook1, expectedBook2);

        actual = target.findByAuthorStartingWith("unknown");
        assertThat(actual).isEmpty();
    }

    @Test
    @Order(1)
    void testAddToUpdateToDelete() {

        Book addBook = new Book(null, "新宿鮫", "大沢在昌");
        Book actual = target.add(addBook);
        assertThat(actual.getId()).isEqualTo(4);

        Book updateBook = actual.clone();
        updateBook.setTitle("update-title");
        updateBook.setAuthor("update-author");

        actual = target.update(updateBook);
        assertThat(actual.getTitle()).isEqualTo("update-title");
        assertThat(actual.getAuthor()).isEqualTo("update-author");

        target.delete(actual.getId());
        assertThat(target.get(actual.getId())).isNull();
    }

    @Test
    @Order(5)
    void testAddOccurValidationError() {
        Book addBook = new Book(null, null, null);
        assertThatThrownBy(() -> target.add(addBook))
            .isInstanceOfSatisfying(
                    HttpClientErrorException.class,
                    e -> assertThat(e.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @Order(5)
    void testUpdateOccurValidationError() {
        Book addBook = new Book(null, null, null);
        assertThatThrownBy(() -> target.update(addBook))
            .isInstanceOfSatisfying(
                    HttpClientErrorException.class,
                    e -> assertThat(e.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @Order(9)
    void testAddOccurDuplicateException() {
        Book addBook = new Book(null, "峠", "司馬遼太郎");
        assertThatThrownBy(() -> target.add(addBook))
                .isInstanceOfSatisfying(
                        HttpClientErrorException.class,
                        e -> assertThat(e.getStatusCode().value()).isEqualTo(HttpStatus.CONFLICT.value()));
    }

    @Test
    @Order(9)
    void testUpdateOccurNotFoundException() {
        Book updateBook = new Book(999, "新宿鮫", "大沢在昌");
        assertThatThrownBy(() -> target.update(updateBook))
                .isInstanceOfSatisfying(
                        HttpClientErrorException.class,
                        e -> assertThat(e.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @Order(9)
    void testDeleteOccurNotFoundException() {
        assertThatThrownBy(() -> target.delete(999))
                .isInstanceOfSatisfying(
                        HttpClientErrorException.class,
                        e -> assertThat(e.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @Order(9)
    void testUpdateOccurConstrainException() {
        assertThatThrownBy(() -> target.update(new Book(3, "燃えよ剣", null)))
                .isInstanceOfSatisfying(
                        HttpClientErrorException.class,
                        e -> assertThat(e.getStatusCode().value()).isEqualTo(HttpStatus.CONFLICT.value()));
    }

    @HttpExchange(url = "/books")
    static interface BookControllerClient {

        @GetExchange("/{id}")
        Book get(@PathVariable int id);

        @GetExchange("/author")
        List<Book> findByAuthorStartingWith(@RequestParam("title") String prefix);

        @PostExchange
        Book add(@RequestBody Book book);

        @PutExchange
        Book update(@RequestBody Book book);

        @DeleteExchange("/{id}")
        void delete(@PathVariable int id);
    }
}
