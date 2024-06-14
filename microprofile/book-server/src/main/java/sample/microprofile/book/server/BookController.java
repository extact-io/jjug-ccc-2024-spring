package sample.microprofile.book.server;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
import sample.microprofile.book.server.exception.DuplicateException;
import sample.microprofile.book.server.exception.NotFoundException;
import sample.microprofile.book.server.repository.BookRepository;

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
    public Book get(@PathParam("id") int id) {
        return repository.get(id);
    }

    @GET
    @Path("/author")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> findByAuthorStartingWith(@QueryParam("prefix") String prefix) {
        return repository.findByAuthorStartingWith(prefix);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Book add(Book book) throws DuplicateException {
        return repository.save(book);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Book update(Book book) throws NotFoundException {
        return repository.save(book);
    }

    @DELETE
    @Path("/{id}")
    public void delete(int id) throws NotFoundException {
        repository.remove(id);
    }
}
