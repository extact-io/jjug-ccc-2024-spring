package sample.microprofile.book.client;

import java.util.List;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import sample.microprofile.book.client.exception.BookClientExceptionMapper;
import sample.microprofile.book.client.header.PropagateJwtHeadersFactory;

@RegisterRestClient(configKey = "book-api")
@RegisterProvider(BookClientExceptionMapper.class)
@RegisterClientHeaders(PropagateJwtHeadersFactory.class)
@Path("books")
public interface BookClient {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    BookDto get(@PathParam("id") int id);

    @GET
    @Path("/author")
    @Produces(MediaType.APPLICATION_JSON)
    List<BookDto> findByAuthorStartingWith(@QueryParam("prefix") String prefix);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    BookDto add(BookDto book);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    BookDto update(BookDto book);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") int id);
}
