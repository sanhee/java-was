package webserver.http.message;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.Const;
import webserver.http.Body;
import webserver.http.header.RequestHeader;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PostMessageTest {

    @ParameterizedTest
    @MethodSource("getHeader")
    void getHeader(String messageText, RequestHeader expectedRequestHeader) {
        PostMessage postMessage = PostMessage.from(messageText);

        assertThat(postMessage)
                .extracting("header")
                .usingRecursiveFieldByFieldElementComparator()
                .contains(expectedRequestHeader);
    }

    static Stream<Arguments> getHeader() {
        return Stream.of(
                Arguments.of(
                        "POST /user/create HTTP/1.1" + Const.CRLF +
                        "Host: localhost:8080" + Const.CRLF +
                        "Connection: keep-alive" + Const.CRLF +
                        "Content-Length: 59" + Const.CRLF +
                        "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                        "Accept: */*" + Const.CRLF +
                        "" + Const.CRLF +
                        "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net" + Const.CRLF,
                        RequestHeader.from(
                                //TODO: 텍스트가 아닌 객체를 생성해서 테스트 해볼 수 있을 듯 Request의 테스트 참고
                                "POST /user/create HTTP/1.1" + Const.CRLF +
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
    @MethodSource("getBody")
    void getBody(String messageText, Body expectedBody) {
        PostMessage postMessage = PostMessage.from(messageText);

        assertThat(postMessage.getBody())
                .isEqualToComparingFieldByFieldRecursively(expectedBody);
    }

    static Stream<Arguments> getBody() {
        return Stream.of(
                Arguments.of("POST /user/create HTTP/1.1" + Const.CRLF +
                             "Host: localhost:8080" + Const.CRLF +
                             "Connection: keep-alive" + Const.CRLF +
                             "Content-Length: 59" + Const.CRLF +
                             "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                             "Accept: */*" + Const.CRLF +
                             "" + Const.CRLF +
                             "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net" + Const.CRLF,
                             Body.from("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net" + System.lineSeparator())
                ), Arguments.of("POST /user/create HTTP/1.1" + Const.CRLF +
                                "" + Const.CRLF +
                                "",
                                Body.from("")
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getMethod")
    void getMethod(String messageText, String expectedRequestMethod) {
        PostMessage postMessage = PostMessage.from(messageText);
        assertThat(postMessage.getMethod()).isEqualTo(expectedRequestMethod);
    }

    static Stream<Arguments> getMethod() {
        return Stream.of(
                Arguments.of("POST /user/create HTTP/1.1" + Const.CRLF +
                             "Host: localhost:8080" + Const.CRLF +
                             "Connection: keep-alive" + Const.CRLF +
                             "Content-Length: 59" + Const.CRLF +
                             "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                             "Accept: */*" + Const.CRLF +
                             "" + Const.CRLF +
                             "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net",
                             "POST"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getParameters")
    void getParameters(String messageText, Map<String, String> expectedParameters) {
        PostMessage postMessage = PostMessage.from(messageText);
        assertThat(postMessage.getParameters())
                .isEqualTo(expectedParameters);
    }

    static Stream<Arguments> getParameters() {
        return Stream.of(
                Arguments.of("POST /user/create HTTP/1.1" + Const.CRLF +
                             "Host: localhost:8080" + Const.CRLF +
                             "Connection: keep-alive" + Const.CRLF +
                             "Content-Length: 59" + Const.CRLF +
                             "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                             "Accept: */*" + Const.CRLF +
                             "" + Const.CRLF +
                             "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net",
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
