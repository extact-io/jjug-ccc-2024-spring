package sample.microprofile.book.server;

import java.util.logging.Logger;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import sample.microprofile.book.server.repository.BookRepository;

public class BuildExtension implements BuildCompatibleExtension {

    private static final Logger LOGGER = Logger.getLogger(BuildExtension.class.getName());

    @Discovery
    public void discoverFrameworkClasses(ScannedClasses scan) {
        Config config = ConfigProvider.getConfig();
        String useClass = config.getValue("use.repository", String.class);
        LOGGER.info("useClass ->" + useClass);
        scan.add(useClass);
    }

    @Enhancement(types = BookRepository.class, withSubtypes = true)
    public void addInterceptorBindingToProcessors(ClassConfig clazz) {
        clazz.addAnnotation(ApplicationScoped.class);
    }
}
