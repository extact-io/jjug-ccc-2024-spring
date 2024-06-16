package sample.microprofile.book.server;

import static org.assertj.core.api.Assertions.*;

import java.net.URI;
import java.util.List;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.testing.junit5.AddConfig;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@HelidonTest
@AddConfig(key = "server.port", value = "7001")
class SecureBookControllerTest {

    private SecureBookControllerClient target;

    @BeforeEach
    void setup() throws Exception {
        target = RestClientBuilder.newBuilder()
                .baseUri(new URI("http://localhost:7001/secure"))
                .build(SecureBookControllerClient.class);
    }

    @Test
    void testSecurityError() {
        assertThatThrownBy(() -> target.findByAuthorStartingWith("abc"))
            .isInstanceOfSatisfying(
                    WebApplicationException.class,
                    e -> assertThat(e.getResponse().getStatus()).isEqualTo(Status.UNAUTHORIZED.getStatusCode()));
    }

    @Path("books")
    static interface SecureBookControllerClient {

        @GET
        @Path("/author")
        @Produces(MediaType.APPLICATION_JSON)
        List<Book> findByAuthorStartingWith(@QueryParam("prefix") String prefix);
    }
}
