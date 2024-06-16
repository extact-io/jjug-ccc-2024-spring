package sample.spring.book.server.config;

import java.net.URI;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartupRunner implements ApplicationRunner {

    private Environment env;
    @Autowired
    private AppInfo appInfo;

    public StartupRunner(Environment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String name = env.getProperty("app.info.name");
        LocalDate createDate = env.getProperty("app.info.create-date", LocalDate.class);
        String company = env.getProperty("app.info.company.name");
        URI url = env.getProperty("app.info.company.url", URI.class);

        log.info("name->%s".formatted(name));
        log.info("createDate->%s".formatted(createDate));
        log.info("company->%s".formatted(company));
        log.info("url->%s".formatted(url));

        log.info(appInfo.toString());
    }
}
