package sample.microprofile.book.server;

import io.helidon.microprofile.testing.junit5.AddConfig;

@AddConfig(key = "use.repository", value = "sample.microprofile.book.server.repository.InMemoryBookRepository")
public class UseInMemoryRepositoryTest extends AbstractBookControllerTest {

}
