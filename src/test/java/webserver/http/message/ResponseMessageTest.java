package webserver.http.message;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                        "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                        "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                        System.lineSeparator() +
                        "Hello World",
                        ResponseHeader.from(
                                "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator()
                        )
                ),
                Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                        "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                        "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator(),
                        ResponseHeader.from(
                                "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator()
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getBody")
    void getBody(String messageText, Body expectedBody) {
        ResponseMessage responseMessage = ResponseMessage.from(messageText);

        assertThat(responseMessage.getBody()).isEqualTo(expectedBody);
    }

    static Stream<Arguments> getBody() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                        "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                        "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                        System.lineSeparator() +
                        "Hello World",
                        Body.from("Hello World")
                ), Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                        "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                        "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                        System.lineSeparator() +
                        "Hello World" + System.lineSeparator() +
                        "Bye World",
                        Body.from("Hello World" + System.lineSeparator() +
                                  "Bye World")
                ), Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                        "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                        "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                        System.lineSeparator() +
                        "Hello World" + System.lineSeparator() +
                        System.lineSeparator() +
                        "Bye World",
                        Body.from("Hello World" + System.lineSeparator() +
                                  System.lineSeparator() +
                                  "Bye World")
                )
        );
    }
}
