package sample.spring.book.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ch.qos.logback.access.tomcat.LogbackValve;

@SpringBootApplication
@EnableJpaRepositories
public class BookApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }

    @Bean
    TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        LogbackValve valve = new LogbackValve();
        valve.setFilename(LogbackValve.DEFAULT_FILENAME);
        tomcatServletWebServerFactory.addContextValves(valve);
        return tomcatServletWebServerFactory;
    }
//
//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.datasource.test1")
//    DataSourceProperties test1DataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.datasource.test1.hikari")
//    HikariDataSource test1DataSource(@Qualifier("test1DataSourceProperties") DataSourceProperties properties) {
//        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
//    }
//
//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.datasource.test1.jpa")
//    public JpaProperties test1JpaProperties() {
//        return new JpaProperties();
//    }
//
//    @Bean
//    @Primary
//    LocalContainerEntityManagerFactoryBean test1EntityManagerFactory(
//            @Qualifier("test1DataSource") DataSource dataSource,
//            @Qualifier("test1JpaProperties") JpaProperties jpaProperties) {
//        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(),
//                jpaProperties.getProperties(), null);
//        return builder
//                .dataSource(dataSource)
//                .packages("sample.spring")
//                .persistenceUnit("test1")
//                .properties(Map.of("hibernate.hbm2ddl.auto", "create"))
//                .build();
//    }
//
//    @Bean
//    @Primary
//    PlatformTransactionManager test1TransactionManager(
//            @Qualifier("test1EntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory.getObject());
//    }
//
//    @Bean
//    DataSourceInitializer test1DataSourceInitializer(@Qualifier("test1DataSource") DataSource dataSource) {
//        DataSourceInitializer initializer = new DataSourceInitializer();
//        initializer.setDataSource(dataSource);
//        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("init-data.sql")));
//        return initializer;
//    }
}
