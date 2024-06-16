package sample.spring.book.server;

import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "use.repository=inmemory")
class UseInMemoryRepositoryTest extends AbstractBookControllerTest {
}
