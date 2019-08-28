package com.grayraccoon.webutils.config.beans.ws;

import com.grayraccoon.webutils.config.beans.entities.UsersEntity;
import com.grayraccoon.webutils.config.beans.services.SimpleUserService;
import com.grayraccoon.webutils.dto.GenericDto;
import com.grayraccoon.webutils.ws.BaseWebService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersWebService extends BaseWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersWebService.class);

    @Autowired
    private SimpleUserService simpleUserService;

    @HystrixCommand(commandKey = "FindAllUsers", groupKey = "Users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/secured/users")
    public GenericDto<List<UsersEntity>> findAllUsers() {
        LOGGER.info("findAllUsers()");
        final List<UsersEntity> users = simpleUserService.findAll();
        LOGGER.info("{} users found.", users.size());
        return GenericDto.<List<UsersEntity>>builder().data(users).build();
    }

    @PostMapping(value = "/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GenericDto<UsersEntity> createUser(
            @RequestBody final UsersEntity usersEntity) {
        LOGGER.info("createUser(): {}", usersEntity);
        final UsersEntity savedUser = simpleUserService.saveUser(usersEntity);
        LOGGER.info("saved user: {}", savedUser);
        return GenericDto.<UsersEntity>builder().data(savedUser).build();
    }

}
