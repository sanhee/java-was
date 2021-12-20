package webserver.http.message;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.Const;
import webserver.http.Body;
import webserver.http.header.RequestHeader;
import webserver.http.startline.RequestLine;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RequestMessageTest {
    @ParameterizedTest
    @MethodSource("fromGetMessage")
    void fromGetMessage(String messageText, GetMessage expectedGetMessage) {
        assertThat(RequestMessage.from(messageText))
                .isEqualToComparingFieldByFieldRecursively(expectedGetMessage);
    }

    static Stream<Arguments> fromGetMessage() {
        return Stream.of(
                Arguments.of(
                        "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Content-Length: 59" + Const.CRLF +
                                "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                                "Accept: */*" + Const.CRLF +
                                "" + Const.CRLF,
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
                                                "Content-Length: 59" + Const.CRLF +
                                                "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                                                "Accept: */*" + Const.CRLF
                                )
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("fromPostMessage")
    void fromPostMessage(String messageText, PostMessage expectedPostMessage) {
        assertThat(RequestMessage.from(messageText))
                .isEqualToComparingFieldByFieldRecursively(expectedPostMessage);
    }

    static Stream<Arguments> fromPostMessage() {
        return Stream.of(
                Arguments.of(
                        "POST /user/create HTTP/1.1" + Const.CRLF +
                        "Host: localhost:8080" + Const.CRLF +
                        "Connection: keep-alive" + Const.CRLF +
                        "Content-Length: 59" + Const.CRLF +
                        "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                        "Accept: */*" + Const.CRLF +
                        "" + Const.CRLF +
                        "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net",
                        PostMessage.from(
                                "POST /user/create HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Content-Length: 59" + Const.CRLF +
                                "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                                "Accept: */*" + Const.CRLF +
                                "" + Const.CRLF +
                                "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net"
                        )
                )
        );
    }
}
