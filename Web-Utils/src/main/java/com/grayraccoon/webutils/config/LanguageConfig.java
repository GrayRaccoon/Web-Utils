package com.grayraccoon.webutils.config;

import com.grayraccoon.webutils.config.components.AcceptLanguageHeaderLocaleResolver;
import com.grayraccoon.webutils.config.properties.LanguageMessagesProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author Heriberto Reyes Esparza
 */
@Configuration
@ConditionalOnProperty(
        value="spring.web-utils.lang-messages.enabled",
        havingValue = "true")
public class LanguageConfig {

    @Bean
    public LocaleResolver localeResolver(
            final LanguageMessagesProperties languageMessagesProperties) {
        final AcceptLanguageHeaderLocaleResolver localeResolver
                = new AcceptLanguageHeaderLocaleResolver(languageMessagesProperties);
        localeResolver.setDefaultLocale(languageMessagesProperties.getParsedDefaultLocale());
        return localeResolver;
    }

    @Bean
    public MessageSource messageSource(
            final LanguageMessagesProperties languageMessagesProperties) {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(languageMessagesProperties.getMessageBasenames());
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultLocale(languageMessagesProperties.getParsedDefaultLocale());
        LocaleContextHolder.setDefaultLocale(languageMessagesProperties.getParsedDefaultLocale());
        return messageSource;
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean getValidator(final MessageSource messageSource) {
        final LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

}
