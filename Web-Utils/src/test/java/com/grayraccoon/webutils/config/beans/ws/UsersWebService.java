package com.grayraccoon.webutils.config.beans.ws;

import com.grayraccoon.webutils.config.beans.entities.UsersEntity;
import com.grayraccoon.webutils.config.beans.services.SimpleUserService;
import com.grayraccoon.webutils.dto.GenericDto;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import com.grayraccoon.webutils.ws.BaseWebService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Heriberto Reyes Esparza
 */
@RestController
public class UsersWebService extends BaseWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersWebService.class);

    @Autowired
    private SimpleUserService simpleUserService;

    @HystrixCommand(commandKey = "findAllUsers", groupKey = "Users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/secured/users")
    public GenericDto<?> findAllUsers() {
        LOGGER.info("findAllUsers()");
        final List<UsersEntity> users = simpleUserService.findAll();
        LOGGER.info("{} users found.", users.size());
        return GenericDto.<List<UsersEntity>>builder().data(users).build();
    }

    @HystrixCommand(fallbackMethod = "findUserFallback",
            commandKey = "findUser",
            groupKey = "Users",
            ignoreExceptions = CustomApiException.class)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/secured/users/{userId}")
    public GenericDto<?> findUser(@PathVariable UUID userId) {
        LOGGER.info("findUser() {}", userId);
        final UsersEntity user = simpleUserService.findUserById(userId);
        LOGGER.info("User {} Found: {}", userId, user);
        return GenericDto.<UsersEntity>builder().data(user).build();
    }

    @HystrixCommand(commandKey = "createUser", groupKey = "Users")
    @PostMapping(value = "/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericDto<?> createUser(
            @RequestBody final UsersEntity usersEntity) {
        LOGGER.info("createUser(): {}", usersEntity);
        final UsersEntity savedUser = simpleUserService.saveUser(usersEntity);
        LOGGER.info("saved user: {}", savedUser);
        return GenericDto.<UsersEntity>builder().data(savedUser).build();
    }

}
