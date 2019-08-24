package com.grayraccoon.webutils.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@ConditionalOnProperty(
        value="spring.web-utils.data.enabled",
        havingValue = "true",
        matchIfMissing = true)
public class UtilsDataSourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilsDataSourceConfig.class);

    private Environment environment;
    @Autowired
    public void setEnvironment(Environment environment) { this.environment = environment; }

    @Value("${spring.web-utils.data.entities-package}")
    private String entitiesPackage;

    /**
     * Creates programmatically the dataSource bean for the postgres DB connection.
     *
     * @return javax.sql.DataSource
     */
    @Primary
    @Bean("utilsDataSource")
    @ConfigurationProperties(prefix="spring.web-utils.data.datasource")
    public DataSource getDataSource() throws URISyntaxException {
        if (!Arrays.asList(environment.getActiveProfiles()).contains("heroku")) {
            return DataSourceBuilder.create().build();
        }
        final String herokuDatabaseUrlEnvName
                = environment.getProperty("spring.web-utils.data.heroku.url-env-name");
        final String schema
                = environment.getProperty("spring.web-utils.data.heroku.db-schema");
        final String dbType
                = environment.getProperty("spring.web-utils.data.heroku.db-type");

        LOGGER.info("getDataSource(): env-name {}", herokuDatabaseUrlEnvName);

        final String dbUrl = System.getenv(herokuDatabaseUrlEnvName);

        LOGGER.info("getDataSource(): env {}", dbUrl);

        final URI dbUri = new URI(dbUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbFormatedUrl = String.format("jdbc:%s://%s:%s%s?currentSchema=%s",
                dbType,
                dbUri.getHost(),
                String.valueOf(dbUri.getPort()),
                dbUri.getPath(),
                schema
        );

        LOGGER.info("user: {}", username);
        LOGGER.info("pass: {}", password);
        LOGGER.info("url: {}", dbFormatedUrl);

        return DataSourceBuilder
                .create()
                .username(username)
                .password(password)
                .url(dbFormatedUrl)
                .build();
    }


    /**
     * Creates programmatically the entityManager bean for the postgres DB connection.
     *
     * @param builder
     * @param dataSource
     * @return org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
     */
    @Primary
    @Bean(name = "utilsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("utilsDataSource") DataSource dataSource
    ) {
        Map<String, Object> props = new HashMap<>();
        Map<String, Object> jpaProperties = new HashMap<>();

        props.put("jpaDialect", org.springframework.orm.jpa.vendor.HibernateJpaDialect.class);
        props.put("jpaProperties", jpaProperties);

        return builder
                .dataSource(dataSource)
                .packages(entitiesPackage)    // Domain Package
                .persistenceUnit("utils")
                .properties(props)
                .build();
    }


    /**
     * Creates programmatically the transactionManager bean for the postgres DB connection.
     *
     * @param entityManagerFactory
     * @return org.springframework.transaction.PlatformTransactionManager
     */
    @Primary
    @Bean(name = "utilsTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("utilsEntityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
