package com.grayraccoon.webutils.config.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Heriberto Reyes Esparza
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "spring.web-utils.lang-messages")
public class LanguageMessagesProperties {

    private boolean enabled;
    private String defaultLocale;
    private List<String> supportedLocales;
    private String[] messageBasenames;

    /**
     * Parses currently configured default locale.
     *
     * @return Parsed locale.
     */
    public Locale getParsedDefaultLocale() {
        return new Locale(defaultLocale);
    }

    /**
     * Parses currently configured supported locales.
     *
     * @return Parsed supported locales.
     */
    public List<Locale> getParsedSupportedLocales() {
        return supportedLocales.stream()
                .map(Locale::new)
                .collect(Collectors.toList());
    }

}
