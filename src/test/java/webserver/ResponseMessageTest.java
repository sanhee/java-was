package webserver;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ResponseMessageTest {

    @ParameterizedTest
    @MethodSource("getHeader")
    void getHeader(String messageText, Header expectedRequestHeader) {
        ResponseMessage responseMessage = ResponseMessage.from(messageText);

        assertThat(responseMessage.getHeader()).isEqualTo(expectedRequestHeader);
    }

    static Stream<Arguments> getHeader() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                                System.lineSeparator() +
                                "Hello World",
                        Header.responseHeaderFrom("HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator())
                ),
                Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator(),
                        Header.responseHeaderFrom("HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator())
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
                )
        );
    }

}
