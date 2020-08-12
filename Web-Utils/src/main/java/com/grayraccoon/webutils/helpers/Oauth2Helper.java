package com.grayraccoon.webutils.helpers;

import com.grayraccoon.webutils.abstracts.BaseUserDetails;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
    @SuppressWarnings("unchecked")
    public static BaseUserDetails getCurrentUserDetails() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final OAuth2Authentication authentication = (OAuth2Authentication)
                Objects.requireNonNull(context).getAuthentication();
        final Authentication userAuthentication = Objects.requireNonNull(authentication).getUserAuthentication();

        if (userAuthentication.getPrincipal() instanceof BaseUserDetails)
            return (BaseUserDetails) userAuthentication.getPrincipal();

        final OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        final Map<String, Object> decodedDetails = (Map<String, Object>) oauthDetails.getDecodedDetails();

        final String rawUserId = String.valueOf(decodedDetails.get("userId"));
        final String email = String.valueOf(decodedDetails.get("email"));
        final String username = String.valueOf(decodedDetails.get("username"));

        return SimpleUserDetails.builder()
                .userId(UUID.fromString(rawUserId))
                .username(username)
                .email(email)
                .build();
    }

    /**
     * Internal private immutable impl of base user details.
     */
    @Value
    @Builder
    private static class SimpleUserDetails implements BaseUserDetails {
        UUID userId;
        String email;
        String username;
    }

}
