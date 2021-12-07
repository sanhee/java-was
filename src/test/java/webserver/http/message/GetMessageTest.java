package webserver.http.message;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.Const;
import webserver.http.header.RequestHeader;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GetMessageTest {
    @ParameterizedTest
    @MethodSource("getHeader")
    void getHeader(String messageText, RequestHeader expectedRequestHeader) {
        GetMessage getMessage = GetMessage.from(messageText);

        assertThat(getMessage)
                .extracting("header")
                .usingRecursiveFieldByFieldElementComparator()
                .contains(expectedRequestHeader);
    }

    static Stream<Arguments> getHeader() {
        return Stream.of(
                //TODO getMessage인데 POST가  성공하는 테스트케이스로 들어가 있음!
                Arguments.of(
                        "GET /user/create HTTP/1.1" + Const.CRLF +
                        "Host: localhost:8080" + Const.CRLF +
                        "Connection: keep-alive" + Const.CRLF +
                        "Content-Length: 59" + Const.CRLF +
                        "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                        "Accept: */*" + Const.CRLF +
                        "" + Const.CRLF,
                        RequestHeader.from(
                                "GET /user/create HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Content-Length: 59" + Const.CRLF +
                                "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                                "Accept: */*" + Const.CRLF +
                                "" + Const.CRLF
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getMethod")
    void getMethod(String messageText, String expectedRequestMethod) {
        GetMessage getMessage = GetMessage.from(messageText);
        assertThat(getMessage.getMethod()).isEqualTo(expectedRequestMethod);
    }

    static Stream<Arguments> getMethod() {
        return Stream.of(
                Arguments.of("GET /user/create HTTP/1.1" + Const.CRLF +
                             "Host: localhost:8080" + Const.CRLF +
                             "Connection: keep-alive" + Const.CRLF +
                             "Content-Length: 59" + Const.CRLF +
                             "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                             "Accept: */*" + Const.CRLF +
                             "" + Const.CRLF,
                             "GET"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getParameters")
    void getParameters(String messageText, Map<String, String> expectedParameters) {
        GetMessage getMessage = GetMessage.from(messageText);
        assertThat(getMessage.getParameters())
                .isEqualTo(expectedParameters);
    }

    static Stream<Arguments> getParameters() {
        return Stream.of(
                Arguments.of(
                        "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1" + Const.CRLF +
                        "Host: localhost:8080" + Const.CRLF +
                        "Connection: keep-alive" + Const.CRLF +
                        "Content-Length: 59" + Const.CRLF +
                        "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                        "Accept: */*" + Const.CRLF +
                        "" + Const.CRLF,
                        new HashMap<String, String>() {{
                            put("userId", "javajigi");
                            put("password", "password");
                            put("name", "박재성");
                            put("email", "javajigi@slipp.net");
                        }}
                )
        );
    }
}
