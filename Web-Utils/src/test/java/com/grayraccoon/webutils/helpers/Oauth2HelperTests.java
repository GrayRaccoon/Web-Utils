package com.grayraccoon.webutils.helpers;

import com.grayraccoon.webutils.abstracts.BaseUserDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Heriberto Reyes Esparza
 */
@ExtendWith(MockitoExtension.class)
public class Oauth2HelperTests {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private OAuth2Authentication authentication;

    @Mock
    private Authentication userAuthentication;

    @Mock
    private OAuth2AuthenticationDetails oauth2Details;

    @Test
    public void test_getCurrentUserDetails_BaseUserDetailsAsPrincipal_Success() {
        final UUID userId = UUID.randomUUID();
        final String email = "user@email.com";
        final String username = "cool_user";

        Mockito.when(securityContext.getAuthentication())
                .thenReturn(authentication);

        Mockito.when(authentication.getUserAuthentication())
                .thenReturn(userAuthentication);

        Mockito.when(userAuthentication.getPrincipal())
                .thenReturn(new BaseUserDetails() {
                    @Override
                    public UUID getUserId() { return userId; }
                    @Override
                    public String getUsername() { return username; }
                    @Override
                    public String getEmail() { return email; }
                });

        SecurityContextHolder.setContext(securityContext);
        final BaseUserDetails currentUserDetails = Oauth2Helper.getCurrentUserDetails();

        Assertions.assertThat(currentUserDetails).isNotNull();
        Assertions.assertThat(currentUserDetails.getUserId()).isNotNull();
        Assertions.assertThat(currentUserDetails.getUserId()).isEqualTo(userId);
        Assertions.assertThat(currentUserDetails.getEmail()).isNotNull();
        Assertions.assertThat(currentUserDetails.getEmail()).isEqualTo(email);
        Assertions.assertThat(currentUserDetails.getUsername()).isNotNull();
        Assertions.assertThat(currentUserDetails.getUsername()).isEqualTo(username);
    }

    @Test
    public void test_getCurrentUserDetails_UsernameAsPrincipal_Success() {
        final UUID userId = UUID.randomUUID();
        final String email = "user@email.com";
        final String username = "cool_user";

        Mockito.when(securityContext.getAuthentication())
                .thenReturn(authentication);

        Mockito.when(authentication.getUserAuthentication())
                .thenReturn(userAuthentication);

        Mockito.when(userAuthentication.getPrincipal()).thenReturn(username);

        Mockito.when(authentication.getDetails())
                .thenReturn(oauth2Details);

        final Map<String, Object> decodedDetails = new HashMap<>();
        decodedDetails.put("userId", userId.toString());
        decodedDetails.put("email", email);
        decodedDetails.put("username", username);

        Mockito.when(oauth2Details.getDecodedDetails()).thenReturn(decodedDetails);

        SecurityContextHolder.setContext(securityContext);
        final BaseUserDetails currentUserDetails = Oauth2Helper.getCurrentUserDetails();

        Assertions.assertThat(currentUserDetails).isNotNull();
        Assertions.assertThat(currentUserDetails.getUserId()).isNotNull();
        Assertions.assertThat(currentUserDetails.getUserId()).isEqualTo(userId);
        Assertions.assertThat(currentUserDetails.getEmail()).isNotNull();
        Assertions.assertThat(currentUserDetails.getEmail()).isEqualTo(email);
        Assertions.assertThat(currentUserDetails.getUsername()).isNotNull();
        Assertions.assertThat(currentUserDetails.getUsername()).isEqualTo(username);
    }

}
