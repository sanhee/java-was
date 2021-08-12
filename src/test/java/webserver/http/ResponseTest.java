package webserver.http;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.http.message.ResponseMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ResponseTest {
    @ParameterizedTest
    @MethodSource("write")
    void write(String message) throws IOException {
        Response response = Response.from(message);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        response.write(outputStream);
        ResponseMessage responseMessage = ResponseMessage.from(outputStream.toString());
        assertThat(response).isEqualTo(new Response(responseMessage));
    }

    static Stream<Arguments> write() {
        return Stream.of(
                Arguments.of(
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                                System.lineSeparator() +
                                "Hello World"
                )
        );
    }
}
