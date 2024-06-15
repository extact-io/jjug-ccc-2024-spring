package sample.spring.book.server.config;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "app.info")
@Data
public class AppInfo {

    private String name;
    private String createDate;
    private CompanyInfo company;

    @Data
    static class CompanyInfo {
        private String name;
        private URI url;
    }
}
