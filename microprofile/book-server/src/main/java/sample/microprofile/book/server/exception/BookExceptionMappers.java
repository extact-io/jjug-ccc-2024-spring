package sample.microprofile.book.server.exception;

import java.util.Map;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;

@ConstrainedTo(RuntimeType.SERVER)
public class BookExceptionMappers {

    @Produces(MediaType.APPLICATION_JSON)
    public static class DuplicateExceptionMapper implements ExceptionMapper<DuplicateException> {

        @Override
        public Response toResponse(DuplicateException exception) {
            return Response
                    .status(Status.CONFLICT)
                    .entity(Map.of("message", exception.getMessage()))
                    .build();
        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    public static class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

        @Override
        public Response toResponse(NotFoundException exception) {
            return Response
                    .status(Status.NOT_FOUND)
                    .entity(Map.of("message", exception.getMessage()))
                    .build();
        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    public static class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

        @Override
        public Response toResponse(ConstraintViolationException exception) {
            exception.printStackTrace();
            return Response
                    .status(Status.BAD_REQUEST)
                    .entity(Map.of("message", exception.getMessage()))
                    .build();
        }
    }

}
