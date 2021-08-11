package webserver;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StatusLineAttributesTest {

    @ParameterizedTest
    @MethodSource("method")
    void method(String desc, StatusLineAttributes statusLineAttributes, String expectedMethod) {
        assertThat(statusLineAttributes.method())
                .as("status line에서 method 가져오기 : %s", desc)
                .isEqualTo(expectedMethod);
    }

    static Stream<Arguments> method() {
        return Stream.of(
                Arguments.of(
                        "GET 메소드",
                        new StatusLineAttributes(
                                new HashMap<String, String>() {{
                                    put("path", "/user/create");
                                    put("method", "GET");
                                    put("protocolVersion", "HTTP/1.1");
                                }}
                        ),
                        "GET"
                )
        );
    }
}
