
package sample.microprofile.book.server;

import static org.assertj.core.api.Assertions.*;

import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import io.helidon.metrics.api.MetricsFactory;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@HelidonTest
class MainTest {

    @Inject
    private MetricRegistry registry;

    @Inject
    private WebTarget target;


    @Test
    void testHealth() {
        Response response = target
                .path("health")
                .request()
                .get();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testMicroprofileMetrics() {
        Message message = target.path("simple-greet/Joe")
                .request()
                .get(Message.class);

        assertThat(message.getMessage()).isEqualTo("Hello Joe");

        Counter counter = registry.counter("personalizedGets");
        double before = counter.getCount();

        message = target.path("simple-greet/Eric")
                .request()
                .get(Message.class);

        assertThat(message.getMessage()).isEqualTo("Hello Eric");
        double after = counter.getCount();
        assertThat(after - before).isEqualTo(1d);
    }

    @AfterAll
    static void clear() {
        MetricsFactory.closeAll();
    }


    @Test
    void testGreet() {
        Message message = target
                .path("simple-greet")
                .request()
                .get(Message.class);
        assertThat(message.getMessage()).isEqualTo("Hello World!");

    }

    @Test
    void testGreetings() {
        Message jsonMessage = target
                .path("greet/Joe")
                .request()
                .get(Message.class);
        assertThat(jsonMessage.getMessage()).isEqualTo("Hello Joe!");

        try (Response r = target
                .path("greet/greeting")
                .request()
                .put(Entity.entity("{\"greeting\" : \"Hola\"}", MediaType.APPLICATION_JSON))) {
            assertThat(r.getStatus()).isEqualTo(204);
        }

        jsonMessage = target
                .path("greet/Jose")
                .request()
                .get(Message.class);
        assertThat(jsonMessage.getMessage()).isEqualTo("Hola Jose!");
    }

}
