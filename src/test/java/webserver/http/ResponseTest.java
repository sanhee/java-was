package webserver.http;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.Const;
import webserver.http.message.ResponseMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ResponseTest {
    @ParameterizedTest
    @MethodSource("write")
    void write(String message, Response expectedResponse) throws IOException {
        Response response = Response.from(message);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        response.write(outputStream);

        assertThat(response).isEqualToComparingFieldByFieldRecursively(expectedResponse);
    }

    static Stream<Arguments> write() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK" + Const.CRLF +
                        "Content-Type: text/html;charset=utf-8" + Const.CRLF +
                        "Content-Length: " + "Hello World".getBytes().length + Const.CRLF +
                        Const.CRLF +
                        "Hello World",
                        new Response(ResponseMessage.from(
                                "HTTP/1.1 200 OK" + Const.CRLF +
                                "Content-Type: text/html;charset=utf-8" + Const.CRLF +
                                "Content-Length: " + "Hello World".getBytes().length + Const.CRLF +
                                Const.CRLF +
                                "Hello World"
                        ))
                )
        );
    }
}
