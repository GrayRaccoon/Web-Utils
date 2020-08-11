package com.grayraccoon.webutils.ws;

import com.grayraccoon.webutils.advice.RestExceptionHandler;
import com.grayraccoon.webutils.config.beans.services.SimpleUserService;
import com.grayraccoon.webutils.config.beans.ws.UsersWebService;
import com.grayraccoon.webutils.errors.ApiError;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import com.grayraccoon.webutils.services.impl.CustomValidatorServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Heriberto Reyes Esparza
 */
@ExtendWith(MockitoExtension.class)
public class WebServicesTests {

    @InjectMocks
    private UsersWebService usersWebService;

    @Mock
    private SimpleUserService simpleUserService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final CustomValidatorServiceImpl customValidatorService =
                new CustomValidatorServiceImpl(factory.getValidator());
        final RestExceptionHandler restExceptionHandler = new RestExceptionHandler();
        restExceptionHandler.setCustomValidatorService(customValidatorService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(usersWebService)
                .setControllerAdvice(restExceptionHandler)
                .build();
    }

    @Test
    public void contextLoads() { }

    @Test
    public void testAdvice_knownException_Success() throws Exception {
        Mockito.when(simpleUserService.findAll()).thenThrow(
                new CustomApiException(ApiError.builder()
                        .message("Some Internal Exception")
                        .build())
        );

        mockMvc.perform(get("/ws/secured/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", Matchers.notNullValue()))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAdvice_knownCause_Success() throws Exception {
        final ConstraintViolationException knownCause = new ConstraintViolationException(Collections.EMPTY_SET);

        Mockito.when(simpleUserService.findAll()).thenThrow(
                new CustomApiException(ApiError.builder()
                        .throwable(knownCause)
                        .build())
        );

        mockMvc.perform(get("/ws/secured/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", Matchers.notNullValue()))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    public void testAdvice_malformedJson_Success() throws Exception {
        mockMvc.perform(post("/ws/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"some\": 123")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", Matchers.notNullValue()))
                .andExpect(jsonPath("$.data").doesNotExist());
    }


}
