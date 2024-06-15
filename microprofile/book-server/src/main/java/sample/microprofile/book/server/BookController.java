package sample.microprofile.book.server;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.ConvertGroup;
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
import sample.microprofile.book.server.Book.Update;
import sample.microprofile.book.server.exception.DuplicateException;
import sample.microprofile.book.server.exception.NotFoundException;

@Path("books")
@ApplicationScoped
public class BookController {

    private BookRepository repository;

    @Inject
    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book get(@NotNull @PathParam("id") int id) {
        return repository.get(id).orElse(null);
    }

    @GET
    @Path("/author")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> findByAuthorStartingWith(@NotBlank @Size(max= 10) @QueryParam("prefix") String prefix) {
        return repository.findByAuthorStartingWith(prefix);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Book add(@Valid Book book) throws DuplicateException {
        return repository.save(book);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Book update(@Valid @ConvertGroup(to = Update.class) Book book) throws NotFoundException {
        return repository.save(book);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@NotNull @PathParam("id") int id) throws NotFoundException {
        repository.remove(id);
    }
}
