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
}
