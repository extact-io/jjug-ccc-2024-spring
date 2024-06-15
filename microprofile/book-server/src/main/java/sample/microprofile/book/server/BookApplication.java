package sample.microprofile.book.server;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import sample.microprofile.book.server.exception.BookExceptionMappers;

@ApplicationPath("")
@ApplicationScoped
public class BookApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new LinkedHashSet<>();
        classes.add(BookController.class);
        classes.add(BookExceptionMappers.DuplicateExceptionMapper.class);
        classes.add(BookExceptionMappers.NotFoundExceptionMapper.class);
        classes.add(BookExceptionMappers.ConstraintViolationExceptionMapper.class);
        return classes;
    }
}
