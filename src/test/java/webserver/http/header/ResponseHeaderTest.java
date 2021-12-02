package webserver.http.header;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.http.attribute.Attributes;
import webserver.http.statusline.ResponseStatusLine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ResponseHeaderTest {
    @ParameterizedTest
    @MethodSource("getAttributes")
    void getAttributes(String headerText, Attributes expectedAttributes) {
        assertThat(ResponseHeader.from(headerText).getAttributes())
                .isEqualTo(expectedAttributes);
    }

    static Stream<Arguments> getAttributes() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                                System.lineSeparator(),
                        Attributes.from(
                                new LinkedHashMap<String, String>() {{
                                    put("Content-Type", "text/html;charset=utf-8");
                                    put("Content-Length", String.valueOf("Hello World".getBytes().length));
                                }}
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getStatusLineAttributes")
    void getStatusLineAttributes(String headerText, ResponseStatusLine expectedResponseStatusLine) {
        assertThat(ResponseHeader.from(headerText))
                .extracting("statusLine")
                .usingRecursiveFieldByFieldElementComparator()
                .contains(expectedResponseStatusLine);
    }

    static Stream<Arguments> getStatusLineAttributes() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                                System.lineSeparator(),
                        new ResponseStatusLine(
                                new HashMap() {{
                                    put("protocolVersion", "HTTP/1.1");
                                    put("statusText", "OK");
                                    put("statusCode", "200");
                                }}
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getBytes")
    void getBytes(String headerText, byte[] expectedHeaderByte) {
        byte[] headerByte = ResponseHeader.from(headerText).getBytes();

        assertAll(
                () -> assertThat(headerByte).isEqualTo(expectedHeaderByte),
                () -> assertThat(new String(headerByte)).isEqualTo(new String(expectedHeaderByte))
        );
    }

    static Stream<Arguments> getBytes() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                                System.lineSeparator(),
                        ("HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                                System.lineSeparator()).getBytes(StandardCharsets.UTF_8)
                )
        );
    }
}
