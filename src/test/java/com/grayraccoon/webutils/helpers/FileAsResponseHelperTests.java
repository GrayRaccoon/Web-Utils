package com.grayraccoon.webutils.helpers;

import com.grayraccoon.webutils.exceptions.CustomApiException;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RunWith(MockitoJUnitRunner.class)
public class FileAsResponseHelperTests {

    private static final String BASIC_STRING_CONTENT = "1234-abcd!";
    private static final String FILENAME = "someFile";

    @Test
    public void writeContentAsFileResponse_String_AllArgs_Success() throws UnsupportedEncodingException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final String content = BASIC_STRING_CONTENT;
        final String contentType = MediaType.TEXT_PLAIN_VALUE;
        final String fileName = FILENAME;

        FileAsResponseHelper.writeContentAsFileResponse(response, content, contentType, fileName);

        validateOutput(response, content, contentType, fileName);
    }

    @Test
    public void writeContentAsFileResponse_String_NoFileName_Success() throws UnsupportedEncodingException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final String content = BASIC_STRING_CONTENT;
        final String contentType = MediaType.TEXT_PLAIN_VALUE;

        FileAsResponseHelper.writeContentAsFileResponse(response, content, contentType);

        validateOutput(response, content, contentType, null);
    }

    @Test
    public void writeContentAsFileResponse_String_NoContentTypeNoFileName_Success() throws UnsupportedEncodingException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final String content = BASIC_STRING_CONTENT;

        FileAsResponseHelper.writeContentAsFileResponse(response, content);

        validateOutput(response, content, null, null);
    }


    @Test
    public void writeContentAsFileResponse_ByteArray_AllArgs_Success() throws UnsupportedEncodingException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final byte[] content = BASIC_STRING_CONTENT.getBytes(StandardCharsets.UTF_8);
        final String contentType = MediaType.TEXT_PLAIN_VALUE;
        final String fileName = FILENAME;

        FileAsResponseHelper.writeContentAsFileResponse(response, content, contentType, fileName);

        validateOutput(response, content, contentType, fileName);
    }

    @Test
    public void writeContentAsFileResponse_ByteArray_NoFileName_Success() throws UnsupportedEncodingException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final byte[] content = BASIC_STRING_CONTENT.getBytes(StandardCharsets.UTF_8);
        final String contentType = MediaType.TEXT_PLAIN_VALUE;

        FileAsResponseHelper.writeContentAsFileResponse(response, content, contentType);

        validateOutput(response, content, contentType, null);
    }

    @Test
    public void writeContentAsFileResponse_ByteArray_NoContentTypeNoFileName_Success() throws UnsupportedEncodingException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final byte[] content = BASIC_STRING_CONTENT.getBytes(StandardCharsets.UTF_8);

        FileAsResponseHelper.writeContentAsFileResponse(response, content);

        validateOutput(response, content, null, null);
    }


    @Test
    public void writeContentAsFileResponse_InputStream_AllArgs_Success() throws UnsupportedEncodingException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final byte[] actualContent = BASIC_STRING_CONTENT.getBytes(StandardCharsets.UTF_8);
        final InputStream content = new ByteArrayInputStream(actualContent);
        final String contentType = MediaType.TEXT_HTML_VALUE;
        final String fileName = FILENAME;

        FileAsResponseHelper.writeContentAsFileResponse(response, content,
                contentType, fileName);

        validateOutput(response, actualContent, contentType, fileName);
    }

    @Test
    public void writeContentAsFileResponse_InputStream_NoFileName_Success() throws UnsupportedEncodingException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final byte[] actualContent = BASIC_STRING_CONTENT.getBytes(StandardCharsets.UTF_8);
        final InputStream content = new ByteArrayInputStream(actualContent);
        final String contentType = MediaType.TEXT_HTML_VALUE;

        FileAsResponseHelper.writeContentAsFileResponse(response, content,
                contentType);

        validateOutput(response, actualContent, contentType, null);
    }

    @Test
    public void writeContentAsFileResponse_InputStream_NoContentTypeNoFileName_Success() throws UnsupportedEncodingException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final byte[] actualContent = BASIC_STRING_CONTENT.getBytes(StandardCharsets.UTF_8);
        final InputStream content = new ByteArrayInputStream(actualContent);

        FileAsResponseHelper.writeContentAsFileResponse(response, content);

        validateOutput(response, actualContent, null, null);
    }

    @Test(expected = CustomApiException.class)
    public void writeContentAsFileResponse_InputStream_AllArgs_Fail() throws IOException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final byte[] actualContent = BASIC_STRING_CONTENT.getBytes(StandardCharsets.UTF_8);

        final InputStream content = Mockito.mock(InputStream.class);
        Mockito.when(
                content.read(ArgumentMatchers.any())
        ).thenThrow(new IOException("Some Crazy Error!"));

        final String contentType = MediaType.TEXT_HTML_VALUE;
        final String fileName = FILENAME;

        FileAsResponseHelper.writeContentAsFileResponse(response, content,
                contentType, fileName);

        validateOutput(response, actualContent, contentType, fileName);
    }


    private static void validateOutput(
            final MockHttpServletResponse response,
            final Object content,
            final String contentType,
            final String fileName
            ) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(contentType)) {
            Assertions.assertThat(response.getContentType()).isNotNull();
            Assertions.assertThat(response.getContentType()).isEqualTo( contentType );
        }

        if (StringUtils.isNotBlank(fileName)) {
            final String contentDisposition = response.getHeader("Content-Disposition");
            Assertions.assertThat(contentDisposition).isNotNull();
            Assertions.assertThat(contentDisposition).startsWith(FileAsResponseHelper.CONTENT_DISPOSITION_PREFIX);
            Assertions.assertThat(contentDisposition).endsWith(fileName);
        }

        Objects.requireNonNull(content);

        if (content instanceof String) {
            final String contentString = (String) content;
            final String resultedContent = response.getContentAsString();
            Assertions.assertThat(resultedContent).isNotNull();
            Assertions.assertThat(resultedContent).isEqualTo(contentString);
        } else if (content instanceof byte[]) {
            final byte[] contentByteArray = (byte[]) content;
            final byte[] resultedContent = response.getContentAsByteArray();
            Assertions.assertThat(resultedContent).isNotNull();
            Assertions.assertThat(resultedContent).isNotEmpty();
            Assertions.assertThat(resultedContent).hasSameSizeAs(contentByteArray);
            Assertions.assertThat(resultedContent).isEqualTo(contentByteArray);
        } else {
            throw new RuntimeException("Invalid ContentType Type.");
        }
    }


}
