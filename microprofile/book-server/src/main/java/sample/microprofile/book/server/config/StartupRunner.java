package sample.microprofile.book.server.config;

import java.util.logging.Logger;

import org.eclipse.microprofile.config.Config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.inject.Inject;
import sample.microprofile.book.server.interceptor.TraceLogInterceptor;

@ApplicationScoped
public class StartupRunner {

    private static final Logger LOGGER = Logger.getLogger(TraceLogInterceptor.class.getName());

    private Config config;
    @Inject
    private AppInfo appInfo;

    @Inject
    public StartupRunner(Config config) {
        this.config = config;
    }

    public void startup(@Observes Startup event) {

        // MP Configの標準で型変換可能なのはprimitive型とClass型のみ
        // https://download.eclipse.org/microprofile/microprofile-config-3.0.3/microprofile-config-spec-3.0.3.html#_built_in_converters
        String name = config.getValue("app.info.name", String.class);
        String createDate = config.getValue("app.info.create-date", String.class);
        String company = config.getValue("app.info.company.name", String.class);
        String url = config.getValue("app.info.company.url", String.class);

        LOGGER.info("name->%s".formatted(name));
        LOGGER.info("createDate->%s".formatted(createDate));
        LOGGER.info("company->%s".formatted(company));
        LOGGER.info("url->%s".formatted(url));

        LOGGER.info(appInfo.toString());
    }
}
