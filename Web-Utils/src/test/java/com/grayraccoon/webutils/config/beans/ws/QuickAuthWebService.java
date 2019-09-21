package com.grayraccoon.webutils.config.beans.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static com.grayraccoon.webutils.config.components.CustomAccessTokenConverter.getExtraInfo;

@RestController
@RequestMapping("/ws")
public class QuickAuthWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuickAuthWebService.class);

    @GetMapping(value = "/authenticated/userId")
    public Map<String, String> getAuthenticatedUserId(Authentication authentication) {
        LOGGER.info("getPrincipalDetails");
        final Map<String,Object> extraInfo = getExtraInfo(authentication);
        final String userId = (String) extraInfo.get("userId");
        LOGGER.info("returning: {}", userId);
        return Collections.singletonMap("userId", userId);
    }

}
