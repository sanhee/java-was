package webserver.http.message;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.Const;
import webserver.http.Body;
import webserver.http.header.Header;
import webserver.http.header.ResponseHeader;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ResponseMessageTest {

    @ParameterizedTest
    @MethodSource("getHeader")
    void getHeader(String messageText, Header expectedRequestHeader) {
        ResponseMessage responseMessage = ResponseMessage.from(messageText);

        assertThat(responseMessage.getHeader())
                .isEqualToComparingFieldByFieldRecursively(expectedRequestHeader);
    }

    static Stream<Arguments> getHeader() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK" + Const.CRLF +
                        "Content-Type: text/html;charset=utf-8" + Const.CRLF +
                        "Content-Length: " + "Hello World".getBytes().length + Const.CRLF +
                        Const.CRLF +
                        "Hello World",
                        ResponseHeader.from(
                                "HTTP/1.1 200 OK" + Const.CRLF +
                                "Content-Type: text/html;charset=utf-8" + Const.CRLF +
                                "Content-Length: " + "Hello World".getBytes().length + Const.CRLF
                        )
                ),
                Arguments.of(
                        "HTTP/1.1 200 OK" + Const.CRLF +
                        "Content-Type: text/html;charset=utf-8" + Const.CRLF +
                        "Content-Length: " + "Hello World".getBytes().length + Const.CRLF,
                        ResponseHeader.from(
                                "HTTP/1.1 200 OK" + Const.CRLF +
                                "Content-Type: text/html;charset=utf-8" + Const.CRLF +
                                "Content-Length: " + "Hello World".getBytes().length + Const.CRLF
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getBody")
    void getBody(String messageText, Body expectedBody) {
        ResponseMessage responseMessage = ResponseMessage.from(messageText);

        assertThat(responseMessage.getBody())
                .isEqualToComparingFieldByFieldRecursively(expectedBody);
    }

    static Stream<Arguments> getBody() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK" + Const.CRLF +
                        "Content-Type: text/html;charset=utf-8" + Const.CRLF +
                        "Content-Length: " + "Hello World".getBytes().length + Const.CRLF +
                        Const.CRLF +
                        "Hello World",
                        Body.from("Hello World")
                ), Arguments.of(
                        "HTTP/1.1 200 OK" + Const.CRLF +
                        "Content-Type: text/html;charset=utf-8" + Const.CRLF +
                        "Content-Length: " + "Hello World".getBytes().length + Const.CRLF +
                        Const.CRLF +
                        "Hello World" + Const.CRLF +
                        "Bye World",
                        Body.from("Hello World" + Const.CRLF +
                                  "Bye World")
                ), Arguments.of(
                        "HTTP/1.1 200 OK" + Const.CRLF +
                        "Content-Type: text/html;charset=utf-8" + Const.CRLF +
                        "Content-Length: " + "Hello World".getBytes().length + Const.CRLF +
                        Const.CRLF +
                        "Hello World" + Const.CRLF +
                        Const.CRLF +
                        "Bye World",
                        Body.from("Hello World" + Const.CRLF +
                                  Const.CRLF +
                                  "Bye World")
                )
        );
    }
}
