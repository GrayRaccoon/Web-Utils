package com.grayraccoon.webutils.config;

import com.zaxxer.hikari.HikariDataSource;
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
import org.springframework.context.annotation.Profile;
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
import java.util.Objects;

/**
 * @author Heriberto Reyes Esparza
 */
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

    @Value("${spring.web-utils.data.persistence-unit}")
    private String persistenceUnit;

    /**
     * Creates programmatically the dataSource bean for the DB connection.
     * Using standard datasource configuration.
     *
     * @return javax.sql.DataSource
     */
    @Primary
    @Bean("utilsDataSource")
    @Profile("!heroku")
    @ConfigurationProperties(prefix="spring.web-utils.data.datasource")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Creates programmatically the dataSource bean for the DB connection.
     * Using Heroku environment configuration.
     *
     * @return javax.sql.DataSource
     */
    @Primary
    @Profile("heroku")
    @Bean("utilsDataSource")
    @ConfigurationProperties(prefix="spring.web-utils.data.heroku-datasource")
    public DataSource getHerokuDataSource() throws URISyntaxException {
        final String jdbcUrlTemplate
                = environment.getProperty("spring.web-utils.data.heroku.jdbc-url-template");
        final String herokuDatabaseUrlEnvName
                = environment.getProperty("spring.web-utils.data.heroku.url-env-name");
        final String schema
                = environment.getProperty("spring.web-utils.data.heroku.db-schema");
        final String dbType
                = environment.getProperty("spring.web-utils.data.heroku.db-type");

        Objects.requireNonNull(herokuDatabaseUrlEnvName);
        Objects.requireNonNull(jdbcUrlTemplate);

        LOGGER.info("getDataSource(): env-name {}", herokuDatabaseUrlEnvName);

        final String dbUrl = environment.getProperty(herokuDatabaseUrlEnvName);

        LOGGER.info("getDataSource(): env {}", dbUrl);

        Objects.requireNonNull(dbUrl);
        final URI dbUri = new URI(dbUrl);

        final String username = dbUri.getUserInfo().split(":")[0];
        final String password = dbUri.getUserInfo().split(":")[1];
        final String path = dbUri.getPath();

        final Map<String, String> dbInfo = new HashMap<String, String>(){{
            put("_DB_TYPE_", dbType);
            put("_HOST_", dbUri.getHost());
            put("_PORT_", String.valueOf(dbUri.getPort()));
            put("_PATH_", path);
            put("_DB_", path.replace("/", ""));
            put("_SCHEMA_", schema);
        }};

        String dbFormattedUrl = jdbcUrlTemplate;
        for (final String key : dbInfo.keySet()) {
            dbFormattedUrl = dbFormattedUrl.replace(key, dbInfo.get(key));
        }

        LOGGER.info("user: {}", username);
        LOGGER.info("pass: {}", password);
        LOGGER.info("url: {}", dbFormattedUrl);

        return DataSourceBuilder
                .create()
                .type(HikariDataSource.class)
                .username(username)
                .password(password)
                .url(dbFormattedUrl)
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
            final EntityManagerFactoryBuilder builder,
            @Qualifier("utilsDataSource") final  DataSource dataSource
    ) {
        final Map<String, Object> props = new HashMap<>();
        final Map<String, Object> jpaProperties = new HashMap<>();

        props.put("jpaDialect", org.springframework.orm.jpa.vendor.HibernateJpaDialect.class);
        props.put("jpaProperties", jpaProperties);

        return builder
                .dataSource(dataSource)
                .packages(entitiesPackage)    // Domain Package
                .persistenceUnit(persistenceUnit)
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
            @Qualifier("utilsEntityManagerFactory") final EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
