package com.grayraccoon.webutils.helpers;

import com.grayraccoon.webutils.errors.ApiError;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Heriberto Reyes Esparza
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileAsResponseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileAsResponseHelper.class);

    public static final String CONTENT_DISPOSITION_PREFIX = "attachment;filename=";

    /**
     * Writes byte[] content as a File into a {@link HttpServletResponse}.
     *
     * @param response Response object where to write attachment as response.
     * @param content Content to write on response as file.
     */
    public static void writeContentAsFileResponse(
            final HttpServletResponse response,
            final byte[] content) {
        writeContentAsFileResponse(response, content, null, null);
    }

    /**
     * Writes byte[] content as a File into a {@link HttpServletResponse}.
     *
     * @param response Response object where to write attachment as response.
     * @param content Content to write on response as file.
     * @param contentType File response content type, this param can be null.
     */
    public static void writeContentAsFileResponse(
            final HttpServletResponse response,
            final byte[] content,
            final String contentType) {
        writeContentAsFileResponse(response, content, contentType, null);
    }

    /**
     * Writes byte[] content as a File into a {@link HttpServletResponse}.
     *
     * @param response Response object where to write attachment as response.
     * @param content Content to write on response as file.
     * @param contentType File response content type, this param can be null.
     * @param filename Default attached file name, this param can be null.
     */
    public static void writeContentAsFileResponse(
            final HttpServletResponse response,
            final byte[] content,
            @Nullable final String contentType,
            @Nullable final String filename) {
        Objects.requireNonNull(content);
        final InputStream contentStream = new ByteArrayInputStream(content);
        writeContentAsFileResponse(response, contentStream, contentType, filename);
    }

    /**
     * Writes {@link String} content as a File into a {@link HttpServletResponse}.
     *
     * @param response Response object where to write attachment as response.
     * @param content Content to write on response as file.
     *                It can be plain text or a base64 encoded binary file.
     */
    public static void writeContentAsFileResponse(
            final HttpServletResponse response,
            final String content) {
        writeContentAsFileResponse(response, content, null, null);
    }

    /**
     * Writes {@link String} content as a File into a {@link HttpServletResponse}.
     *
     * @param response Response object where to write attachment as response.
     * @param content Content to write on response as file.
     *                It can be plain text or a base64 encoded binary file.
     * @param contentType File response content type, this param can be null.
     */
    public static void writeContentAsFileResponse(
            final HttpServletResponse response,
            final String content,
            final String contentType) {
        writeContentAsFileResponse(response, content, contentType, null);
    }

    /**
     * Writes {@link String} content as a File into a {@link HttpServletResponse}.
     *
     * @param response Response object where to write attachment as response.
     * @param content Content to write on response as file.
     *                It can be plain text or a base64 encoded binary file.
     * @param contentType File response content type, this param can be null.
     * @param filename Default attached file name, this param can be null.
     */
    public static void writeContentAsFileResponse(
            final HttpServletResponse response,
            final String content,
            @Nullable final String contentType,
            @Nullable final String filename) {
        final InputStream contentStream = IOUtils.toInputStream(content, StandardCharsets.UTF_8);
        writeContentAsFileResponse(response, contentStream, contentType, filename);
    }


    /**
     * Writes {@link InputStream} content as a File into a {@link HttpServletResponse}.
     *
     * @param response Response object where to write attachment as response.
     * @param content Content to write on response as file.
     */
    public static void writeContentAsFileResponse(
            final HttpServletResponse response,
            final InputStream content) {
        writeContentAsFileResponse(response, content, null, null);
    }

    /**
     * Writes {@link InputStream} content as a File into a {@link HttpServletResponse}.
     *
     * @param response Response object where to write attachment as response.
     * @param content Content to write on response as file.
     * @param contentType File response content type, this param can be null.
     */
    public static void writeContentAsFileResponse(
            final HttpServletResponse response,
            final InputStream content,
            final String contentType) {
        writeContentAsFileResponse(response, content, contentType, null);
    }

    /**
     * Writes {@link InputStream} content as a File into a {@link HttpServletResponse}.
     *
     * @param response Response object where to write attachment as response.
     * @param content Content to write on response as file.
     * @param contentType File response content type, this param can be null.
     * @param filename Default attached file name, this param can be null.
     */
    public static void writeContentAsFileResponse(
            final HttpServletResponse response,
            final InputStream content,
            @Nullable final String contentType,
            @Nullable final String filename) {
        Objects.requireNonNull(response);
        Objects.requireNonNull(content);
        if (contentType != null) {
            response.setContentType(contentType);
        }
        if (filename != null) {
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        }
        try {
            IOUtils.copy(content, response.getOutputStream());
            response.flushBuffer();
            content.close();
        } catch (IOException ex) {
            throw new CustomApiException(ApiError.builder()
                    .throwable(ex)
                    .message("Error while writing response.")
                    .build());
        }
    }

}
