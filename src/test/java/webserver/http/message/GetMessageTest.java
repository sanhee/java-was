package webserver.http.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.Const;
import webserver.http.header.RequestHeader;
import webserver.http.startline.RequestLine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GetMessageTest {
    @ParameterizedTest
    @MethodSource("getHeader")
    void getHeader(GetMessage getMessage, RequestHeader expectedRequestHeader) {
        assertThat(getMessage)
                .extracting("header")
                .usingRecursiveFieldByFieldElementComparator()
                .contains(expectedRequestHeader);
    }

    static Stream<Arguments> getHeader() {
        return Stream.of(
                Arguments.of(
                        new GetMessage(
                                RequestLine.from(Arrays.asList(
                                        "GET",
                                        "/users",
                                        "HTTP/1.1"
                                )),
                                RequestHeader.from(
                                        "GET /users HTTP/1.1" + Const.CRLF +
                                                "Host: localhost:8080" + Const.CRLF +
                                                "Connection: keep-alive" + Const.CRLF +
                                                "Accept: */*" + Const.CRLF
                                )
                        ),
                        RequestHeader.from(
                                "GET /users HTTP/1.1" + Const.CRLF +
                                        "Host: localhost:8080" + Const.CRLF +
                                        "Connection: keep-alive" + Const.CRLF +
                                        "Accept: */*" + Const.CRLF +
                                        "" + Const.CRLF
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getMethod")
    void getMethod(GetMessage getMessage, String expectedRequestMethod) {
        assertThat(getMessage.getMethod()).isEqualTo(expectedRequestMethod);
    }

    static Stream<Arguments> getMethod() {
        return Stream.of(
                Arguments.of(
                        new GetMessage(
                                RequestLine.from(Arrays.asList(
                                        "GET",
                                        "/users",
                                        "HTTP/1.1"
                                )),
                                RequestHeader.from(
                                        "GET /users HTTP/1.1" + Const.CRLF +
                                                "Host: localhost:8080" + Const.CRLF +
                                                "Connection: keep-alive" + Const.CRLF +
                                                "Accept: */*" + Const.CRLF
                                )
                        ),
                        "GET"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getParameters")
    void getParameters(GetMessage getMessage, Map<String, String> expectedParameters) {
        assertThat(getMessage.getParameters())
                .isEqualTo(expectedParameters);
    }

    static Stream<Arguments> getParameters() {
        return Stream.of(
                Arguments.of(
                        new GetMessage(
                                RequestLine.from(Arrays.asList(
                                        "GET",
                                        "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net",
                                        "HTTP/1.1"
                                )),
                                RequestHeader.from(
                                        "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1" + Const.CRLF +
                                                "Host: localhost:8080" + Const.CRLF +
                                                "Connection: keep-alive" + Const.CRLF +
                                                "Accept: */*" + Const.CRLF
                                )
                        ),
                        new HashMap<String, String>() {{
                            put("userId", "javajigi");
                            put("password", "password");
                            put("name", "박재성");
                            put("email", "javajigi@slipp.net");
                        }}
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getPath")
    void getPath(String desc, GetMessage getMessage, String expectedPath) {
        String actualPath = getMessage.getPath();

        assertThat(actualPath).as("getPath" + desc)
                .isEqualTo(expectedPath);
    }

    static Stream<Arguments> getPath() {
        return Stream.of(
                Arguments.of(
                        "쿼리스트링이 없는 GET 메세지",
                        new GetMessage(
                                RequestLine.from(Arrays.asList(
                                        "GET",
                                        "/user/create",
                                        "HTTP/1.1"
                                )),
                                RequestHeader.from(
                                        "GET /user/create HTTP/1.1" + Const.CRLF +
                                                "Host: localhost:8080" + Const.CRLF +
                                                "Connection: keep-alive" + Const.CRLF +
                                                "Accept: */*" + Const.CRLF
                                )
                        ),
                        "/user/create"
                ),
                Arguments.of(
                        "쿼리스트링이 포함된 GET 메세지 path를 출력할때도 쿼리스트링이 포함되지 않아야 함",
                        new GetMessage(
                                RequestLine.from(Arrays.asList(
                                        "GET",
                                        "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net",
                                        "HTTP/1.1"
                                )),
                                RequestHeader.from(
                                        "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1" + Const.CRLF +
                                                "Host: localhost:8080" + Const.CRLF +
                                                "Connection: keep-alive" + Const.CRLF +
                                                "Accept: */*" + Const.CRLF
                                )
                        ),
                        "/user/create"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getRequestLine")
    void getRequestLine(GetMessage getMessage, RequestLine expectedRequestLine) {
        Assertions.assertThat(getMessage)
                .extracting("requestLine")
                .usingRecursiveFieldByFieldElementComparator()
                .contains(expectedRequestLine);
    }

    static Stream<Arguments> getRequestLine() {
        return Stream.of(
                Arguments.of(
                        new GetMessage(
                                RequestLine.from(Arrays.asList(
                                        "GET",
                                        "/",
                                        "HTTP/1.1"
                                )),
                                RequestHeader.from(
                                        "GET /user/create HTTP/1.1" + Const.CRLF +
                                                "Host: localhost:8080" + Const.CRLF +
                                                "Connection: keep-alive" + Const.CRLF +
                                                "Content-Length: 59" + Const.CRLF +
                                                "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                                                "Accept: */*" + Const.CRLF)
                        ),
                        new RequestLine(
                                new HashMap<String, String>() {{
                                    put("method", "GET");
                                    put("path", "/");
                                    put("protocolVersion", "HTTP/1.1");
                                }}
                        )
                )
        );
    }
}
