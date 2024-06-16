package sample.microprofile.book.server.secure;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.microprofile.auth.LoginConfig;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import sample.microprofile.book.server.exception.BookExceptionMappers;

@LoginConfig(authMethod = "MP-JWT")
@ApplicationPath("secure")
@ApplicationScoped
public class BookApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new LinkedHashSet<>();
        classes.add(SecureBookController.class);
        classes.add(BookExceptionMappers.DuplicateExceptionMapper.class);
        classes.add(BookExceptionMappers.NotFoundExceptionMapper.class);
        classes.add(BookExceptionMappers.ConstraintViolationExceptionMapper.class);
        return classes;
    }
}
