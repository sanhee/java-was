package util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.HttpRequestUtils.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class HttpRequestUtilsTest {
    @ParameterizedTest
    @MethodSource
    void parseQueryString(String queryString, String expectedUserId, String expectedPassword) {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);

        assertAll(
                () -> assertThat(parameters.get("userId")).isEqualTo(expectedUserId),
                () -> assertThat(parameters.get("password")).isEqualTo(expectedPassword)
        );
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> parseQueryString() {
        return Stream.of(
                Arguments.of("userId=javajigi", "javajigi", null),
                Arguments.of("userId=javajigi&password=password2", "javajigi", "password2")
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseQueryString_null(Map<String, String> parameters) {
        assertThat(parameters.isEmpty()).isTrue();
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> parseQueryString_null() {
        return Stream.of(
                Arguments.of(HttpRequestUtils.parseQueryString(null)),
                Arguments.of(HttpRequestUtils.parseQueryString("")),
                Arguments.of(HttpRequestUtils.parseQueryString(" "))
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseQueryString_invalid(String queryString, String expectedUserId, String expectedPassword) {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);

        assertAll(
                () -> assertThat(parameters.get("userId")).isEqualTo(expectedUserId),
                () -> assertThat(parameters.get("password")).isEqualTo(expectedPassword)
        );
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> parseQueryString_invalid() {
        return Stream.of(
                Arguments.of("userId=javajigi&password", "javajigi", null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseCookies(String cookies, String expectedLogined, String expectedJSessionId, String expectedSession) {
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);

        assertAll(
                () -> assertThat(parameters.get("logined")).isEqualTo(expectedLogined),
                () -> assertThat(parameters.get("JSessionId")).isEqualTo(expectedJSessionId),
                () -> assertThat(parameters.get("session")).isEqualTo(expectedSession)
        );
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> parseCookies() {
        return Stream.of(
                Arguments.of("logined=true; JSessionId=1234", "true", "1234", null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void getKeyValue(Pair pair, Pair expectedPair) {
        assertThat(pair).isEqualTo(expectedPair);
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> getKeyValue() {
        return Stream.of(
                Arguments.of(
                        HttpRequestUtils.getKeyValue("userId=javajigi", "="),
                        new Pair("userId", "javajigi")
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void getKeyValue_invalid(Pair pair, Pair expectedPair) {
        assertThat(pair).isEqualTo(expectedPair);
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> getKeyValue_invalid() {
        return Stream.of(
                Arguments.of(
                        HttpRequestUtils.getKeyValue("userId", "="),
                        null
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseHeader(Pair pair, Pair expectedPair) {
        assertThat(pair).isEqualTo(expectedPair);
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> parseHeader() {
        return Stream.of(
                Arguments.of(
                        HttpRequestUtils.parseHeader("Content-Length: 59"),
                        new Pair("Content-Length", "59")
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseStatusLine(String statusLine, List<String> expectedParsedStatusLine) {
        assertThat(HttpRequestUtils.parseStartLine(statusLine)).isEqualTo(expectedParsedStatusLine);
    }

    static Stream<Arguments> parseStatusLine() {
        return Stream.of(
                Arguments.of(
                        "GET /user/create HTTP/1.1",
                        Arrays.asList("GET", "/user/create", "HTTP/1.1")
                )
        );
    }
}
