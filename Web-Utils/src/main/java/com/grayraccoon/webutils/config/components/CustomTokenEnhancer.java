package com.grayraccoon.webutils.config.components;

import com.grayraccoon.webutils.abstracts.BaseUserDetails;
import com.grayraccoon.webutils.abstracts.BaseUserService;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {

    private BaseUserService baseUserService;

    public CustomTokenEnhancer(BaseUserService baseUserService) {
        this.baseUserService = baseUserService;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();

        String username = oAuth2Authentication.getName();
        try {
            final BaseUserDetails user = baseUserService.findByUsernameOrEmail(username);

            final OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();

            additionalInfo.put("userId", user.getUserId().toString());
            additionalInfo.put("username", user.getUsername());
            additionalInfo.put("email", user.getEmail());
            additionalInfo.put("clientId", oAuth2Request.getClientId());

            ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
        } catch (CustomApiException ex) {
            // User doesn't exist
        }
        return oAuth2AccessToken;
    }

}
