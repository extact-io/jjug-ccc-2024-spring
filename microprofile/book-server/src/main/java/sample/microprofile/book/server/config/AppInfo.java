package sample.microprofile.book.server.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.ToString;

@ApplicationScoped
@ToString
public class AppInfo {

    @Inject
    @ConfigProperty(name = "app.info.name")
    private String name;

    @Inject
    @ConfigProperty(name = "app.info.create-date")
    private String createDate;

    @Inject
    @ConfigProperty(name = "app.info.company.name")
    private String company;

    @Inject
    @ConfigProperty(name = "app.info.company.url")
    private String url;
}
