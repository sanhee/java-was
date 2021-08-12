package webserver.http.message;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.http.header.Header;
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

        assertThat(getMessage.getHeader()).isEqualTo(expectedRequestHeader);
    }

    static Stream<Arguments> getHeader() {
        return Stream.of(
                //TODO getMessage인데 POST가  성공하는 테스트케이스로 들어가 있음!
                Arguments.of("POST /user/create HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 59" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator(),
                        Header.requestHeaderFrom("POST /user/create HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 59" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator())
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
                Arguments.of("GET /user/create HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 59" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator(),
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
                        "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 59" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator(),
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
