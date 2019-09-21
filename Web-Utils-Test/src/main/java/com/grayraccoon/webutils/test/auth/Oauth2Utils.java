package com.grayraccoon.webutils.test.auth;

import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Heriberto Reyes Esparza
 */
public final class Oauth2Utils {

    public static final Oauth2Params oauth2Params = new Oauth2Params();

    private Oauth2Utils() {}

    public static String getUserAccessToken(MockMvc mockMvc, String username, String password) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", oauth2Params.getGrant_type());
        params.add("client_id", oauth2Params.getClient_id());
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(oauth2Params.getClient_id(),oauth2Params.getSecret_id()))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.access_token", Matchers.notNullValue()));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();

        return jsonParser.parseMap(resultString).get("access_token").toString();
    }


    public static Authentication getOauthTestAuthentication() {
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(getOauth2Request(), getAuthentication());
        OAuth2AuthenticationDetails authDetails = new OAuth2AuthenticationDetails(new MockHttpServletRequest());
        authDetails.setDecodedDetails(oAuth2Authentication.getUserAuthentication().getDetails());
        oAuth2Authentication.setDetails( authDetails );
        return oAuth2Authentication;
    }

    public static OAuth2Request getOauth2Request () {
        String clientId = oauth2Params.getClient_id();
        Map<String, String> requestParameters = Collections.emptyMap();
        boolean approved = true;
        String redirectUrl = "http://my-redirect-url.com";
        Set<String> responseTypes = Collections.emptySet();
        Set<String> scopes = Collections.emptySet();
        Set<String> resourceIds = Collections.singleton(oauth2Params.getResource_id());
        Map<String, Serializable> extensionProperties = Collections.emptyMap();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("whatever");

        OAuth2Request oAuth2Request = new OAuth2Request(requestParameters, clientId, authorities,
                approved, scopes, resourceIds, redirectUrl, responseTypes, extensionProperties);

        return oAuth2Request;
    }

    public static Authentication getAuthentication() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("USER", "ADMIN");

        User userPrincipal = new User("admin",
                "",true,true,
                true,true, authorities);

        TestingAuthenticationToken token = new TestingAuthenticationToken(userPrincipal, null, authorities);
        token.setAuthenticated(true);
        token.setDetails(tokenDetails());

        return token;
    }

    public static Map<String, String> tokenDetails() {
        HashMap<String, String> details = new HashMap<>();
        details.put("userId", "01e3d8d5-1119-4111-b3d0-be6562ca5914");
        details.put("username", "admin");
        details.put("name", "Some User");
        return details;
    }

    public static Map<String, Object> getExtraInfo(Authentication auth) {
        OAuth2AuthenticationDetails oauthDetails
                = (OAuth2AuthenticationDetails) auth.getDetails();
        //noinspection unchecked
        return (Map<String, Object>) oauthDetails
                .getDecodedDetails();
    }

}
