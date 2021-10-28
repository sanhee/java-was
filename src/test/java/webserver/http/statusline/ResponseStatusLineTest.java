package webserver.http.statusline;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.http.Attribute;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseStatusLineTest {

    @ParameterizedTest
    @MethodSource("getStatusCode")
    void getStatusCode(String desc, ResponseStatusLine responseStatusLineAttributes, String expectedStatusCode) {
        assertThat(responseStatusLineAttributes.getStatusCode())
                .as("status line에서 status code 가져오기 : %s", desc)
                .isEqualTo(expectedStatusCode);
    }

    static Stream<Arguments> getStatusCode() {
        return Stream.of(
                Arguments.of(
                        "200 OK",
                        new ResponseStatusLine(
                                Attribute.from(
                                        new HashMap<String, String>() {{
                                            put("protocolVersion", "HTTP/1.1");
                                            put("statusCode", "200");
                                            put("statusText", "OK");
                                        }}
                                )
                        ),
                        "200"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getStatusText")
    void getStatusText(String desc, ResponseStatusLine responseStatusLineAttributes, String expectedStatusText) {
        assertThat(responseStatusLineAttributes.getStatusText())
                .as("status line에서 status text 가져오기 : %s", desc)
                .isEqualTo(expectedStatusText);
    }

    static Stream<Arguments> getStatusText() {
        return Stream.of(
                Arguments.of(
                        "200 OK",
                        new ResponseStatusLine(
                                Attribute.from(
                                        new HashMap<String, String>() {{
                                            put("protocolVersion", "HTTP/1.1");
                                            put("statusCode", "200");
                                            put("statusText", "OK");
                                        }}
                                )
                        ),
                        "OK"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getProtocol")
    void getProtocol(String desc, ResponseStatusLine responseStatusLine, String expectedMethod) {
        assertThat(responseStatusLine.getProtocol())
                .as("status line에서 protocol 가져오기 : %s", desc)
                .isEqualTo(expectedMethod);
    }

    static Stream<Arguments> getProtocol() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1",
                        new ResponseStatusLine(
                                Attribute.from(
                                        new HashMap<String, String>() {{
                                            put("protocolVersion", "HTTP/1.1");
                                            put("statusCode", "200");
                                            put("statusText", "OK");
                                        }}
                                )
                        ),
                        "HTTP/1.1"
                )
        );
    }
}
