package sample.microprofile.book.client.exception;

import java.util.List;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import jakarta.annotation.Priority;
import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Priority(Priorities.USER)
@ConstrainedTo(RuntimeType.CLIENT)
public class BookClientExceptionMapper implements ResponseExceptionMapper<RuntimeException> {

    private static final List<Integer> HANDLE_STATUS = List.of(
            Status.CONFLICT.getStatusCode(),
            Status.NOT_FOUND.getStatusCode(),
            Status.BAD_REQUEST.getStatusCode());

    @Override
    public boolean handles(int status, MultivaluedMap<String, Object> headers) {
        return HANDLE_STATUS.contains(status);
    }

    @Override
    public RuntimeException toThrowable(Response response) {

        ErrorMessage message = response.readEntity(ErrorMessage.class);
        Status status = Status.fromStatusCode(response.getStatus());

        return switch (status) {
            case Status.CONFLICT -> new DuplicateClientException(message);
            case Status.NOT_FOUND -> new NotFoundClientException(message);
            case Status.BAD_REQUEST -> new ValidationClientException(message);
            default ->
                throw new IllegalArgumentException("Unexpected value: " + response.getStatus());
        };
    }

    @Setter @Getter @ToString
    public static class ErrorMessage {
        private String message;
    }
}
