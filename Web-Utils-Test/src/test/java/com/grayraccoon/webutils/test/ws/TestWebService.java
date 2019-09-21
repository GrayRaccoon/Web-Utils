package com.grayraccoon.webutils.test.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.grayraccoon.webutils.test.auth.Oauth2Utils.getExtraInfo;

/**
 * @author Heriberto Reyes Esparza
 */
@RestController
@RequestMapping("/ws")
public class TestWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestWebService.class);

    @GetMapping("/authenticated/principal")
    public Map<String,Object> getPrincipalDetails(Authentication authentication) {
        LOGGER.info("getPrincipalDetails");
        final Map<String,Object> extraInfo = getExtraInfo(authentication);
        LOGGER.info("returning: {}", extraInfo);
        return extraInfo;
    }

}
