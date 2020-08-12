package com.grayraccoon.webutils.config.beans.ws;

import com.grayraccoon.webutils.abstracts.BaseUserDetails;
import com.grayraccoon.webutils.helpers.Oauth2Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/ws")
public class QuickAuthWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuickAuthWebService.class);

    @GetMapping(value = "/authenticated/userId")
    public Map<String, ?> getAuthenticatedUserId() {
        LOGGER.info("getPrincipalDetails");
        final BaseUserDetails currentUserDetails = Oauth2Helper.getCurrentUserDetails();
        final UUID userId = currentUserDetails.getUserId();
        LOGGER.info("returning: {}", userId);
        return Collections.singletonMap("userId", userId);
    }

}
