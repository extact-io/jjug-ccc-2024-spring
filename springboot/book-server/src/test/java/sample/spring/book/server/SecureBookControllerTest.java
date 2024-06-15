package sample.spring.book.server;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.main.banner-mode=off")
class SecureBookControllerTest {

    private SecureBookControllerClient target;

    @BeforeEach
    void beforeEach(@Value("${local.server.port}") int port) {

        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:%s".formatted(port))
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        target = factory.createClient(SecureBookControllerClient.class);
    }

    @Test
    void testSecurityError() {
        assertThatThrownBy(() -> target.findByAuthorStartingWith("abc"))
            .isInstanceOfSatisfying(
                    HttpClientErrorException.class,
                    e -> assertThat(e.getStatusCode().value()).isEqualTo(HttpStatus.UNAUTHORIZED.value()));
    }

    @HttpExchange(url = "/secure/books")
    static interface SecureBookControllerClient {

        @GetExchange("/author")
        List<Book> findByAuthorStartingWith(@RequestParam("title") String prefix);
    }
}
