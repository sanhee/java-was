package webserver.http.statusline;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.http.attribute.Attributes;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RequestStatusLineTest {

    @ParameterizedTest
    @MethodSource("getMethod")
    void getMethod(String desc, RequestStatusLine requestStatusLine, String expectedMethod) {
        assertThat(requestStatusLine.getMethod())
                .as("status line에서 method 가져오기 : %s", desc)
                .isEqualTo(expectedMethod);
    }

    static Stream<Arguments> getMethod() {
        return Stream.of(
                Arguments.of(
                        "GET 메소드",
                        new RequestStatusLine(
                                new Attributes() {{
                                    add("path", "/user/create");
                                    add("method", "GET");
                                    add("protocolVersion", "HTTP/1.1");
                                }}
                        ),
                        "GET"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getPath")
    void getPath(String desc, RequestStatusLine requestStatusLine, String expectedPath) {
        assertThat(requestStatusLine.getPath())
                .as("status line에서 path 가져오기 : %s", desc)
                .isEqualTo(expectedPath);
    }

    static Stream<Arguments> getPath() {
        return Stream.of(
                Arguments.of(
                        "쿼리스트링이 없는 path",
                        new RequestStatusLine(
                                new Attributes() {{
                                    add("path", "/user/create");
                                    add("method", "GET");
                                    add("protocolVersion", "HTTP/1.1");
                                }}
                        ),
                        "/user/create"
                ), Arguments.of(
                        "쿼리스트링이 있는 path",
                        new RequestStatusLine(
                                new Attributes() {{
                                    add("path", "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
                                    add("method", "GET");
                                    add("protocolVersion", "HTTP/1.1");
                                }}
                        ),
                        "/user/create"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getProtocol")
    void getProtocol(String desc, RequestStatusLine requestStatusLine, String expectedMethod) {
        assertThat(requestStatusLine.getProtocol())
                .as("status line에서 protocol 가져오기 : %s", desc)
                .isEqualTo(expectedMethod);
    }

    static Stream<Arguments> getProtocol() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1",
                        new RequestStatusLine(
                                new Attributes() {{
                                    add("path", "/user/create");
                                    add("method", "GET");
                                    add("protocolVersion", "HTTP/1.1");
                                }}
                        ),
                        "HTTP/1.1"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getQueryString")
    void getQueryString(String desc, RequestStatusLine requestStatusLine, String expectedQueryString) {
        assertThat(requestStatusLine.getQueryString())
                .as("status line에서 쿼리스트링 가져오기 : %s", desc)
                .isEqualTo(expectedQueryString);
    }

    static Stream<Arguments> getQueryString() {
        return Stream.of(
                Arguments.of(
                        "쿼리스트링이 없는 path",
                        new RequestStatusLine(
                                new Attributes() {{
                                    add("path", "/user/create");
                                    add("method", "GET");
                                    add("protocolVersion", "HTTP/1.1");
                                }}
                        ),
                        ""
                ), Arguments.of(
                        "쿼리스트링이 있는 path",
                        new RequestStatusLine(
                                new Attributes() {{
                                    add("path", "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
                                    add("method", "GET");
                                    add("protocolVersion", "HTTP/1.1");
                                }}
                        ),
                        "userId=javajigi&password=password&name=박재성&email=javajigi@slipp.net"
                )
        );
    }
}
