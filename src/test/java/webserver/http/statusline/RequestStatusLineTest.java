package webserver.http.statusline;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RequestStatusLineTest {

    @ParameterizedTest
    @MethodSource("method")
    void method(String desc, RequestStatusLine requestStatusLine, String expectedMethod) {
        assertThat(requestStatusLine.method())
                .as("status line에서 method 가져오기 : %s", desc)
                .isEqualTo(expectedMethod);
    }

    static Stream<Arguments> method() {
        return Stream.of(
                Arguments.of(
                        "GET 메소드",
                        new RequestStatusLine(
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

    @ParameterizedTest
    @MethodSource("path")
    void path(String desc, RequestStatusLine requestStatusLine, String expectedPath) {
        assertThat(requestStatusLine.path())
                .as("status line에서 path 가져오기 : %s", desc)
                .isEqualTo(expectedPath);
    }

    static Stream<Arguments> path() {
        return Stream.of(
                Arguments.of(
                        "쿼리스트링이 없는 path",
                        new RequestStatusLine(
                                new HashMap<String, String>() {{
                                    put("path", "/user/create");
                                    put("method", "GET");
                                    put("protocolVersion", "HTTP/1.1");
                                }}
                        ),
                        "/user/create"
                ), Arguments.of(
                        "쿼리스트링이 있는 path",
                        new RequestStatusLine(
                                new HashMap<String, String>() {{
                                    put("path", "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
                                    put("method", "GET");
                                    put("protocolVersion", "HTTP/1.1");
                                }}
                        ),
                        "/user/create"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("protocol")
    void protocol(String desc, RequestStatusLine requestStatusLine, String expectedMethod) {
        assertThat(requestStatusLine.protocol())
                .as("status line에서 protocol 가져오기 : %s", desc)
                .isEqualTo(expectedMethod);
    }

    static Stream<Arguments> protocol() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1",
                        new RequestStatusLine(
                                new HashMap<String, String>() {{
                                    put("path", "/user/create");
                                    put("method", "GET");
                                    put("protocolVersion", "HTTP/1.1");
                                }}
                        ),
                        "HTTP/1.1"
                )
        );
    }
}
