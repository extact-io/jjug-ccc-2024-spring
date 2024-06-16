package sample.spring.book.server;

import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "use.repository=jpa")
class UseJpaRepositoryTest extends AbstractBookControllerTest {
}
