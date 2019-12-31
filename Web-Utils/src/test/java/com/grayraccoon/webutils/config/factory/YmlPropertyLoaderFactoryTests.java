package com.grayraccoon.webutils.config.factory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author Heriberto Reyes Esparza
 */
@ExtendWith(MockitoExtension.class)
public class YmlPropertyLoaderFactoryTests {

    // Constants
    private static final String RESOURCE_NAME_VALID = "sample.yml";
    private static final String RESOURCE_NAME_INVALID = "sample_missing_file.yml";


    // Mocks
    @InjectMocks
    private YmlPropertyLoaderFactory ymlPropertyLoaderFactory;


    @BeforeEach
    public void setUpBeforeEach() throws Exception {}

    @BeforeAll
    static void setUpBeforeAll() throws Exception {}

    @Test
    public void contextLoads() {
        Assertions.assertThat(ymlPropertyLoaderFactory).isNotNull();
    }

    @Test
    public void loadYamlIntoProperties_validResource_Success() throws FileNotFoundException {
        final EncodedResource resource = buildValidEncodedResource();

        final Properties properties = ymlPropertyLoaderFactory
                .loadYamlIntoProperties(resource);

        Assertions.assertThat(properties).isNotNull();

        Assertions.assertThat(properties.getProperty("sample.prop1")).isNotNull();
        Assertions.assertThat(properties.getProperty("sample.prop1")).isEqualTo("Nice");

        Assertions.assertThat(properties.getProperty("sample.prop2")).isNotNull();
        Assertions.assertThat(properties.getProperty("sample.prop2")).isEqualTo("Content");
    }

    @Test
    public void loadYamlIntoProperties_fileNotFound_Fail() {
        final EncodedResource resource = buildInvalidEncodedResource();
        org.junit.jupiter.api.Assertions.assertThrows(FileNotFoundException.class,
                () -> ymlPropertyLoaderFactory.loadYamlIntoProperties(resource));
    }

    @Test
    public void loadYamlIntoProperties_randomException_Fail() {
        final EncodedResource resource = buildRandomExceptionEncodedResource();
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
                () -> ymlPropertyLoaderFactory.loadYamlIntoProperties(resource));
    }

    @Test
    public void createPropertySource_validResourceWithName_Success() throws IOException {
        final String propertySourceName = "sample";
        final EncodedResource resource = buildValidEncodedResource();

        final PropertySource<?> propertySource = ymlPropertyLoaderFactory
                .createPropertySource(propertySourceName, resource);

        Assertions.assertThat(propertySource).isNotNull();
        Assertions.assertThat(propertySource.getName()).isNotNull();
        Assertions.assertThat(propertySource.getName()).isEqualTo(propertySourceName);
    }

    @Test
    public void createPropertySource_validResourceNoName_Success() throws IOException {
        final EncodedResource resource = buildValidEncodedResource();

        final PropertySource<?> propertySource = ymlPropertyLoaderFactory
                .createPropertySource(null, resource);

        Assertions.assertThat(propertySource).isNotNull();
        Assertions.assertThat(propertySource.getName()).isNotNull();
        Assertions.assertThat(propertySource.getName()).isEqualTo(RESOURCE_NAME_VALID);
    }


    // Object builders

    private static EncodedResource buildValidEncodedResource() {
        return buildSimpleEncodedResource(RESOURCE_NAME_VALID);
    }

    private static EncodedResource buildInvalidEncodedResource() {
        return buildSimpleEncodedResource(RESOURCE_NAME_INVALID);
    }

    private static EncodedResource buildRandomExceptionEncodedResource() {
        final EncodedResource encodedResource = Mockito.mock(EncodedResource.class);
        Mockito.when(encodedResource.getResource())
                .thenThrow(new IllegalStateException("Random Exception other than FileNotFound..."));
        return encodedResource;
    }

    private static EncodedResource buildSimpleEncodedResource(final String resourceName) {
        return new EncodedResource(new ClassPathResource(resourceName), StandardCharsets.UTF_8);
    }

}
