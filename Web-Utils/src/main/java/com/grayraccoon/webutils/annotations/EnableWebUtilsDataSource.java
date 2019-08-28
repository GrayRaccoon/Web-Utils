package com.grayraccoon.webutils.annotations;

import com.grayraccoon.webutils.config.UtilsDataSourceConfig;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Heriberto Reyes Esparza
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(UtilsDataSourceConfig.class)
@EnableJpaRepositories(
        entityManagerFactoryRef = "utilsEntityManagerFactory",
        transactionManagerRef = "utilsTransactionManager"
)
public @interface EnableWebUtilsDataSource { }
