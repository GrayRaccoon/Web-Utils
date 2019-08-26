package com.grayraccoon.webutils.ws;

import com.grayraccoon.webutils.exceptions.CustomApiException;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/ws")
@DefaultProperties(
        ignoreExceptions = CustomApiException.class,
        defaultFallback = "defaultFallback")
public class BaseWebService { }
