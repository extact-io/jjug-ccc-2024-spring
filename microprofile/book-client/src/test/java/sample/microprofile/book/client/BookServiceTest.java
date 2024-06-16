package sample.microprofile.book.client;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.helidon.microprofile.testing.junit5.AddConfig;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import sample.microprofile.book.client.exception.DuplicateClientException;
import sample.microprofile.book.client.exception.NotFoundClientException;

@HelidonTest
@AddConfig(key = "server.port", value = "7001")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookServiceTest {

    @Inject
    private BookService target;

    private final BookDto expectedBook1 = new BookDto(1, "燃えよ剣", "司馬遼太郎");
    private final BookDto expectedBook2 = new BookDto(2, "峠", "司馬遼太郎");

    @Test
    @Order(1)
    void testGet() {

        BookDto actual = target.get(1);
        assertThat(actual).isEqualTo(expectedBook1);
        actual = target.get(999);
        assertThat(actual).isNull();
    }

    @Test
    @Order(1)
    void testFindByAuthorStartingWith() {

        List<BookDto> actual = target.findByAuthorStartingWith("司馬");
        assertThat(actual).containsExactly(expectedBook1, expectedBook2);

        actual = target.findByAuthorStartingWith("unknown");
        assertThat(actual).isEmpty();
    }

    @Test
    @Order(1)
    void testAddToUpdateToDelete() {

        BookDto addBook = new BookDto(null, "新宿鮫", "大沢在昌");
        BookDto actual = target.add(addBook);
        assertThat(actual.getId()).isEqualTo(4);

        BookDto updateBook = actual;
        updateBook.setTitle("update-title");
        updateBook.setAuthor("update-author");

        actual = target.update(updateBook);
        assertThat(actual.getTitle()).isEqualTo("update-title");
        assertThat(actual.getAuthor()).isEqualTo("update-author");

        target.delete(actual.getId());
        assertThat(target.get(actual.getId())).isNull();
    }

    @Test
    @Order(9)
    void testAddOccurDuplicateException() {
        BookDto addBook = new BookDto(null, "峠", "司馬遼太郎");
        assertThatThrownBy(() -> target.add(addBook))
                .isInstanceOfSatisfying(
                        DuplicateClientException.class,
                        e -> assertThat(e.getMessage()).contains("message"));
    }

    @Test
    @Order(9)
    void testUpdateOccurNotFoundException() {
        BookDto updateBook = new BookDto(999, "新宿鮫", "大沢在昌");
        assertThatThrownBy(() -> target.update(updateBook))
                .isInstanceOfSatisfying(
                        NotFoundClientException.class,
                        e -> assertThat(e.getMessage()).contains("message"));
    }

    @Test
    @Order(9)
    void testDeleteOccurNotFoundException() {
        assertThatThrownBy(() -> target.delete(999))
                .isInstanceOfSatisfying(
                        NotFoundClientException.class,
                        e -> assertThat(e.getMessage()).contains("message"));
    }
}
