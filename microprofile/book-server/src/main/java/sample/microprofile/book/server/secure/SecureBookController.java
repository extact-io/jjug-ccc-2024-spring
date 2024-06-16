package sample.microprofile.book.server.secure;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import sample.microprofile.book.server.Book;
import sample.microprofile.book.server.BookRepository;

@Path("books")
@ApplicationScoped
public class SecureBookController {

    private BookRepository repository;
    private JsonWebToken jwt;

    @Inject
    public SecureBookController(BookRepository repository, JsonWebToken jwt) {
        this.repository = repository;
        this.jwt = jwt;
    }

    @GET
    @Path("/author")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("MEMBER")
    public List<Book> findByAuthorStartingWith(@NotBlank @Size(max= 10) @QueryParam("prefix") String prefix) {
        return repository.findByAuthorStartingWith(jwt.getName()); // 自著を検索
    }
}
