package com.grayraccoon.webutils.helpers;

import com.grayraccoon.webutils.abstracts.BaseUserDetails;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Objects;

/**
 * @author Heriberto Reyes Esparza
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Oauth2Helper {

    /**
     * Gets current user {@link BaseUserDetails} from Security Context.
     *
     * @return Found base user details.
     */
    public static BaseUserDetails getCurrentUserDetails() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final OAuth2Authentication authentication = (OAuth2Authentication)
                Objects.requireNonNull(context).getAuthentication();
        final Authentication userAuthentication = Objects.requireNonNull(authentication).getUserAuthentication();
        return (BaseUserDetails) userAuthentication.getPrincipal();
    }

}
