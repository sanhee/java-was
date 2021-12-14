package webserver.http.header;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.Const;
import webserver.http.attribute.Attributes;
import webserver.http.startline.RequestStatusLine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class RequestHeaderTest {

    @ParameterizedTest
    @MethodSource("getAttributes")
    void getAttributes(String headerText, Attributes expectedAttributes) {
        assertThat(RequestHeader.from(headerText).getAttributes())
                .isEqualTo(expectedAttributes);
    }

    static Stream<Arguments> getAttributes() {
        return Stream.of(
                Arguments.of(
                        "GET / HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Cache-Control: max-age=0" + Const.CRLF +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + Const.CRLF +
                                "sec-ch-ua-mobile: ?0" + Const.CRLF +
                                "Upgrade-Insecure-Requests: 1" + Const.CRLF +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + Const.CRLF +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + Const.CRLF +
                                "Sec-Fetch-Site: none" + Const.CRLF +
                                "Sec-Fetch-Mode: navigate" + Const.CRLF +
                                "Sec-Fetch-User: ?1" + Const.CRLF +
                                "Sec-Fetch-Dest: document" + Const.CRLF +
                                "Accept-Encoding: gzip, deflate, br" + Const.CRLF +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + Const.CRLF +
                                "Cookie: Idea-1c77831=5ced54c8-cabd-4355-ae5a-97b17f9d7443" + Const.CRLF +
                                Const.CRLF,
                        Attributes.from(new LinkedHashMap<String, String>() {{
                                            put("Host", "localhost:8080");
                                            put("Connection", "keep-alive");
                                            put("Cache-Control", "max-age=0");
                                            put("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
                                            put("sec-ch-ua-mobile", "?0");
                                            put("Upgrade-Insecure-Requests", "1");
                                            put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
                                            put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                                            put("Sec-Fetch-Site", "none");
                                            put("Sec-Fetch-Mode", "navigate");
                                            put("Sec-Fetch-User", "?1");
                                            put("Sec-Fetch-Dest", "document");
                                            put("Accept-Encoding", "gzip, deflate, br");
                                            put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
                                            put("Cookie", "Idea-1c77831=5ced54c8-cabd-4355-ae5a-97b17f9d7443");
                                        }}
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getMethod")
    void getMethod(String headerText, String expectedMethod) {
        assertThat(RequestHeader.from(headerText).getMethod())
                .isEqualTo(expectedMethod);
    }

    static Stream<Arguments> getMethod() {
        return Stream.of(
                Arguments.of(
                        "GET / HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Cache-Control: max-age=0" + Const.CRLF +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + Const.CRLF +
                                "sec-ch-ua-mobile: ?0" + Const.CRLF +
                                "Upgrade-Insecure-Requests: 1" + Const.CRLF +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + Const.CRLF +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + Const.CRLF +
                                "Sec-Fetch-Site: none" + Const.CRLF +
                                "Sec-Fetch-Mode: navigate" + Const.CRLF +
                                "Sec-Fetch-User: ?1" + Const.CRLF +
                                "Sec-Fetch-Dest: document" + Const.CRLF +
                                "Accept-Encoding: gzip, deflate, br" + Const.CRLF +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + Const.CRLF +
                                "Cookie: Idea-1c77831=5ced54c8-cabd-4355-ae5a-97b17f9d7443" + Const.CRLF +
                                Const.CRLF,
                        "GET"
                ), Arguments.of(
                        "POST /user/create HTTP/1.1" + Const.CRLF +
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
    @MethodSource("getStatusLineAttributes")
    void getStatusLineAttributes(String headerText, RequestStatusLine expectedRequestStatusLine) {
        assertThat(RequestHeader.from(headerText))
                .extracting("statusLine")
                .usingRecursiveFieldByFieldElementComparator()
                .contains(expectedRequestStatusLine);
    }

    static Stream<Arguments> getStatusLineAttributes() {
        return Stream.of(
                Arguments.of("GET / HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Cache-Control: max-age=0" + Const.CRLF +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + Const.CRLF +
                                "sec-ch-ua-mobile: ?0" + Const.CRLF +
                                "Upgrade-Insecure-Requests: 1" + Const.CRLF +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + Const.CRLF +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + Const.CRLF +
                                "Sec-Fetch-Site: none" + Const.CRLF +
                                "Sec-Fetch-Mode: navigate" + Const.CRLF +
                                "Sec-Fetch-User: ?1" + Const.CRLF +
                                "Sec-Fetch-Dest: document" + Const.CRLF +
                                "Accept-Encoding: gzip, deflate, br" + Const.CRLF +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + Const.CRLF +
                                "Cookie: Idea-1c77831=5ced54c8-cabd-4355-ae5a-97b17f9d7443" + Const.CRLF +
                                Const.CRLF,
                        new RequestStatusLine(
                                new HashMap<String, String>() {{
                                    put("method", "GET");
                                    put("path", "/");
                                    put("protocolVersion", "HTTP/1.1");
                                }}
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getBytes")
    void getBytes(String headerText, byte[] expectedHeaderByte) {
        byte[] headerByte = RequestHeader.from(headerText).getBytes();

        assertAll(
                () -> assertThat(headerByte).isEqualTo(expectedHeaderByte),
                () -> assertThat(new String(headerByte)).isEqualTo(new String(expectedHeaderByte))
        );

    }

    static Stream<Arguments> getBytes() {
        return Stream.of(
                Arguments.of(
                        "GET / HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Cache-Control: max-age=0" + Const.CRLF +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + Const.CRLF +
                                "sec-ch-ua-mobile: ?0" + Const.CRLF +
                                "Upgrade-Insecure-Requests: 1" + Const.CRLF +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + Const.CRLF +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + Const.CRLF +
                                "Sec-Fetch-Site: none" + Const.CRLF +
                                "Sec-Fetch-Mode: navigate" + Const.CRLF +
                                "Sec-Fetch-User: ?1" + Const.CRLF +
                                "Sec-Fetch-Dest: document" + Const.CRLF +
                                "Accept-Encoding: gzip, deflate, br" + Const.CRLF +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + Const.CRLF +
                                "Cookie: Idea-1c77831=5ced54c8-cabd-4355-ae5a-97b17f9d7443" + Const.CRLF +
                                Const.CRLF,
                        ("GET / HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Cache-Control: max-age=0" + Const.CRLF +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + Const.CRLF +
                                "sec-ch-ua-mobile: ?0" + Const.CRLF +
                                "Upgrade-Insecure-Requests: 1" + Const.CRLF +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + Const.CRLF +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + Const.CRLF +
                                "Sec-Fetch-Site: none" + Const.CRLF +
                                "Sec-Fetch-Mode: navigate" + Const.CRLF +
                                "Sec-Fetch-User: ?1" + Const.CRLF +
                                "Sec-Fetch-Dest: document" + Const.CRLF +
                                "Accept-Encoding: gzip, deflate, br" + Const.CRLF +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + Const.CRLF +
                                "Cookie: Idea-1c77831=5ced54c8-cabd-4355-ae5a-97b17f9d7443" + Const.CRLF +
                                Const.CRLF).getBytes(StandardCharsets.UTF_8)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getPath")
    void getPath(String desc, RequestStatusLine statusLine, String expectedPath) {
        RequestHeader requestHeader = new RequestHeader(statusLine, Attributes.from(new HashMap<>()));

        String actualPath = requestHeader.getPath();

        assertThat(actualPath).as("리퀘스트 헤더에서 path 가져오기 : %s", desc)
                .isEqualTo(expectedPath);
    }

    static Stream<Arguments> getPath() {
        return Stream.of(
                Arguments.of(
                        "쿼리스트링이 없는 GET 메세지",
                        new RequestStatusLine(
                                new HashMap() {{
                                    put("path", "/user/create");
                                    put("method", "GET");
                                    put("protocolVersion", "HTTP/1.1");
                                }}
                        ),
                        "/user/create"
                ),
                Arguments.of(
                        "쿼리스트링이 포함된 GET 메세지 path를 출력할때도 쿼리스트링이 포함되지 않아야 함",
                        new RequestStatusLine(
                                new HashMap() {{
                                    put("path", "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
                                    put("method", "GET");
                                    put("protocolVersion", "HTTP/1.1");
                                }}
                        ),
                        "/user/create"
                )
        );
    }
}
