package webserver.http.statusline;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseStatusLineTest {

    @ParameterizedTest
    @MethodSource("statusCode")
    void statusCode(String desc, ResponseStatusLine requestStatusLineAttributes, String expectedStatusCode) {
        assertThat(requestStatusLineAttributes.statusCode())
                .as("status line에서 status code 가져오기 : %s", desc)
                .isEqualTo(expectedStatusCode);
    }

    static Stream<Arguments> statusCode() {
        return Stream.of(
                Arguments.of(
                        "200 OK",
                        new ResponseStatusLine(
                                new HashMap<String, String>() {{
                                    put("protocolVersion", "HTTP/1.1");
                                    put("statusCode", "200");
                                    put("statusText", "OK");
                                }}
                        ),
                        "200"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("statusText")
    void statusText(String desc, ResponseStatusLine requestStatusLineAttributes, String expectedStatusText) {
        assertThat(requestStatusLineAttributes.statusText())
                .as("status line에서 status text 가져오기 : %s", desc)
                .isEqualTo(expectedStatusText);
    }

    static Stream<Arguments> statusText() {
        return Stream.of(
                Arguments.of(
                        "200 OK",
                        new ResponseStatusLine(
                                new HashMap<String, String>() {{
                                    put("protocolVersion", "HTTP/1.1");
                                    put("statusCode", "200");
                                    put("statusText", "OK");
                                }}
                        ),
                        "OK"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("protocol")
    void protocol(String desc, ResponseStatusLine requestStatusLine, String expectedMethod) {
        assertThat(requestStatusLine.protocol())
                .as("status line에서 protocol 가져오기 : %s", desc)
                .isEqualTo(expectedMethod);
    }

    static Stream<Arguments> protocol() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1",
                        new ResponseStatusLine(
                                new HashMap<String, String>() {{
                                    put("protocolVersion", "HTTP/1.1");
                                    put("statusCode", "200");
                                    put("statusText", "OK");
                                }}
                        ),
                        "HTTP/1.1"
                )
        );
    }
}
