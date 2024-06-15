package sample.microprofile.book.server;

import static org.assertj.core.api.Assertions.*;

import java.net.URI;
import java.util.List;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.helidon.microprofile.testing.junit5.AddConfig;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@HelidonTest
@AddConfig(key = "server.port", value = "7001")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
abstract class AbstractBookControllerTest {

    private BookControllerClient target;

    private final Book expectedBook1 = new Book(1, "燃えよ剣", "司馬遼太郎");
    private final Book expectedBook2 = new Book(2, "峠", "司馬遼太郎");

    @BeforeEach
    void setup() throws Exception {
        target = RestClientBuilder.newBuilder()
                .baseUri(new URI("http://localhost:7001"))
                .build(BookControllerClient.class);
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
                    WebApplicationException.class,
                    e -> assertThat(e.getResponse().getStatus()).isEqualTo(Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @Order(5)
    void testUpdateOccurValidationError() {
        Book addBook = new Book(null, null, null);
        assertThatThrownBy(() -> target.update(addBook))
            .isInstanceOfSatisfying(
                    WebApplicationException.class,
                    e -> assertThat(e.getResponse().getStatus()).isEqualTo(Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @Order(9)
    void testAddOccurDuplicateException() {
        Book addBook = new Book(null, "峠", "司馬遼太郎");
        assertThatThrownBy(() -> target.add(addBook))
            .isInstanceOfSatisfying(
                    WebApplicationException.class,
                    e -> assertThat(e.getResponse().getStatus()).isEqualTo(Status.CONFLICT.getStatusCode()));
    }

    @Test
    @Order(9)
    void testUpdateOccurNotFoundException() {
        Book updateBook = new Book(999, "新宿鮫", "大沢在昌");
        assertThatThrownBy(() -> target.update(updateBook))
            .isInstanceOfSatisfying(
                    WebApplicationException.class,
                    e -> assertThat(e.getResponse().getStatus()).isEqualTo(Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    @Order(9)
    void testDeleteOccurNotFoundException() {
        assertThatThrownBy(() -> target.delete(999))
            .isInstanceOfSatisfying(
                    WebApplicationException.class,
                    e -> assertThat(e.getResponse().getStatus()).isEqualTo(Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    @Order(9)
    void testUpdateOccurConstrainException() {
        assertThatThrownBy(() -> target.update(new Book(3, "燃えよ剣", null)))
            .isInstanceOfSatisfying(
                    WebApplicationException.class,
                    e -> assertThat(e.getResponse().getStatus()).isEqualTo(Status.CONFLICT.getStatusCode()));
    }

    @Path("books")
    static interface BookControllerClient {

        @GET
        @Path("/{id}")
        @Produces(MediaType.APPLICATION_JSON)
        Book get(@PathParam("id") int id);

        @GET
        @Path("/author")
        @Produces(MediaType.APPLICATION_JSON)
        List<Book> findByAuthorStartingWith(@QueryParam("prefix") String prefix);

        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        Book add(Book book);

        @PUT
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        Book update(Book book);

        @DELETE
        @Path("/{id}")
        void delete(@PathParam("id") int id);
    }
}
