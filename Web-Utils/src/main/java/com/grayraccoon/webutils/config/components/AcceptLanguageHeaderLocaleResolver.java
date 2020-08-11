package com.grayraccoon.webutils.config.components;

import com.grayraccoon.webutils.config.properties.LanguageMessagesProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

/**
 * @author Heriberto Reyes Esparza
 */
public class AcceptLanguageHeaderLocaleResolver extends AcceptHeaderLocaleResolver {

    private final LanguageMessagesProperties languageMessagesProperties;

    public AcceptLanguageHeaderLocaleResolver(
            final LanguageMessagesProperties languageMessagesProperties) {
        this.languageMessagesProperties = languageMessagesProperties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Locale resolveLocale(final HttpServletRequest request) {
        final String headerLang = request.getHeader("Accept-Language");

        final Locale defaultLocale = languageMessagesProperties.getParsedDefaultLocale();
        final List<Locale> supportedLocales = languageMessagesProperties.getParsedSupportedLocales();
        return StringUtils.isBlank(headerLang) ? defaultLocale:
                Locale.lookup(Locale.LanguageRange.parse(headerLang), supportedLocales);
    }
}

