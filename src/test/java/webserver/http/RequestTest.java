package webserver.http;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.http.header.RequestHeader;
import webserver.http.message.GetMessage;
import webserver.http.message.PostMessage;
import webserver.http.message.RequestMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RequestTest {

    @ParameterizedTest
    @MethodSource("from")
    void from(String inputMessage, RequestMessage expectedRequestMessage) {
        Request request = Request.from(inputMessage);

        assertThat(request.getRequestMessage())
                .isEqualToComparingFieldByFieldRecursively(expectedRequestMessage);
    }

    static Stream<Arguments> from() {
        return Stream.of(
                Arguments.of(
                        "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1" + System.lineSeparator() +
                        "Host: localhost:8080" + System.lineSeparator() +
                        "Connection: keep-alive" + System.lineSeparator() +
                        "Content-Length: 59" + System.lineSeparator() +
                        "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                        "Accept: */*" + System.lineSeparator() +
                        "" + System.lineSeparator(),
                        new GetMessage(RequestHeader.of(
                                Arrays.asList(
                                        "GET",
                                        "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net",
                                        "HTTP/1.1"
                                ),
                                new HashMap() {{
                                    put("Host", "localhost:8080");
                                    put("Connection", "keep-alive");
                                    put("Content-Length", "59");
                                    put("Content-Type", "application/x-www-form-urlencoded");
                                    put("Accept", "*/*");
                                }}
                        ))
                ), Arguments.of(
                        "POST /user/create HTTP/1.1" + System.lineSeparator() +
                        "Host: localhost:8080" + System.lineSeparator() +
                        "Connection: keep-alive" + System.lineSeparator() +
                        "Content-Length: 59" + System.lineSeparator() +
                        "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                        "Accept: */*" + System.lineSeparator() +
                        "" + System.lineSeparator() +
                        "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net",
                        new PostMessage(
                                RequestHeader.of(
                                        Arrays.asList(
                                                "POST",
                                                "/user/create",
                                                "HTTP/1.1"
                                        ),
                                        new HashMap() {{
                                            put("Host", "localhost:8080");
                                            put("Connection", "keep-alive");
                                            put("Content-Length", "59");
                                            put("Content-Type", "application/x-www-form-urlencoded");
                                            put("Accept", "*/*");
                                        }}
                                ),
                                Body.from("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net")
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void path(String desc, GetMessage getMessage, String expectedPath) {
        String actualPath = new Request(getMessage).path();

        assertThat(actualPath).as(desc)
                              .isEqualTo(expectedPath);
    }

    static Stream<Arguments> path() {
        return Stream.of(
                Arguments.of(
                        "쿼리스트링이 없는 GET 메세지",
                        new GetMessage(RequestHeader.of(
                                Arrays.asList(
                                        "GET",
                                        "/user/create",
                                        "HTTP/1.1"
                                ),
                                new HashMap() {{
                                    put("Host", "localhost:8080");
                                    put("Connection", "keep-alive");
                                    put("Content-Length", "59");
                                    put("Content-Type", "application/x-www-form-urlencoded");
                                    put("Accept", "*/*");
                                }}
                        )),
                        "/user/create"
                ), Arguments.of(
                        "쿼리스트링이 포함된 GET 메세지 path를 출력할때도 쿼리스트링이 포함되지 않아야 함",
                        new GetMessage(RequestHeader.of(
                                Arrays.asList(
                                        "GET",
                                        "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net",
                                        "HTTP/1.1"
                                ),
                                new HashMap() {{
                                    put("Host", "localhost:8080");
                                    put("Connection", "keep-alive");
                                    put("Content-Length", "59");
                                    put("Content-Type", "application/x-www-form-urlencoded");
                                    put("Accept", "*/*");
                                }}
                        )),
                        "/user/create"
                )
        );
    }


    @ParameterizedTest
    @MethodSource("pathExtension")
    void pathExtension(String desc, Request request, String expectedExtension) {
        Assertions.assertThat(request.pathExtension())
                  .as("status line에서 path의 확장자 가져오기 : %s", desc)
                  .isEqualTo(expectedExtension);
    }

    static Stream<Arguments> pathExtension() {
        return Stream.of(
                Arguments.of(
                        "확장자가 있는 path",
                        new Request(new GetMessage(RequestHeader.of(
                                Arrays.asList(
                                        "GET",
                                        "/user/create.html",
                                        "HTTP/1.1"
                                ),
                                new HashMap() {{
                                    put("Host", "localhost:8080");
                                    put("Connection", "keep-alive");
                                    put("Content-Length", "59");
                                    put("Content-Type", "application/x-www-form-urlencoded");
                                    put("Accept", "*/*");
                                }}
                        ))),
                        "html"
                ), Arguments.of(
                        "확장자가 있는 path /로 끝남",
                        new Request(new GetMessage(RequestHeader.of(
                                Arrays.asList(
                                        "GET",
                                        "/user/create.html/",
                                        "HTTP/1.1"
                                ),
                                new HashMap() {{
                                    put("Host", "localhost:8080");
                                    put("Connection", "keep-alive");
                                    put("Content-Length", "59");
                                    put("Content-Type", "application/x-www-form-urlencoded");
                                    put("Accept", "*/*");
                                }}
                        ))),
                        "html"
                ), Arguments.of(
                        "확장자와 쿼리스트링이 함께 있는 path",
                        new Request(new GetMessage(RequestHeader.of(
                                Arrays.asList(
                                        "GET",
                                        "/user/create.html?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net",
                                        "HTTP/1.1"
                                ),
                                new HashMap() {{
                                    put("Host", "localhost:8080");
                                    put("Connection", "keep-alive");
                                    put("Content-Length", "59");
                                    put("Content-Type", "application/x-www-form-urlencoded");
                                    put("Accept", "*/*");
                                }}
                        ))),
                        "html"
                ), Arguments.of(
                        "확장자와 쿼리스트링이 함께 있는 path /로 끝남",
                        new Request(new GetMessage(RequestHeader.of(
                                Arrays.asList(
                                        "GET",
                                        "/user/create.html?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net/",
                                        "HTTP/1.1"
                                ),
                                new HashMap() {{
                                    put("Host", "localhost:8080");
                                    put("Connection", "keep-alive");
                                    put("Content-Length", "59");
                                    put("Content-Type", "application/x-www-form-urlencoded");
                                    put("Accept", "*/*");
                                }}
                        ))),
                        "html"
                ), Arguments.of(
                        "확장자가 없는 path",
                        new Request(new GetMessage(RequestHeader.of(
                                Arrays.asList(
                                        "GET",
                                        "/user/create",
                                        "HTTP/1.1"
                                ),
                                new HashMap() {{
                                    put("Host", "localhost:8080");
                                    put("Connection", "keep-alive");
                                    put("Content-Length", "59");
                                    put("Content-Type", "application/x-www-form-urlencoded");
                                    put("Accept", "*/*");
                                }}
                        ))),
                        ""
                ), Arguments.of(
                        "root만 있는 path",
                        new Request(new GetMessage(RequestHeader.of(
                                Arrays.asList(
                                        "GET",
                                        "/",
                                        "HTTP/1.1"
                                ),
                                new HashMap() {{
                                    put("Host", "localhost:8080");
                                    put("Connection", "keep-alive");
                                    put("Content-Length", "59");
                                    put("Content-Type", "application/x-www-form-urlencoded");
                                    put("Accept", "*/*");
                                }}
                        ))),
                        ""
                ), Arguments.of(
                        "비어 있는 path",
                        new Request(new GetMessage(RequestHeader.of(
                                Arrays.asList(
                                        "GET",
                                        "",
                                        "HTTP/1.1"
                                ),
                                new HashMap() {{
                                    put("Host", "localhost:8080");
                                    put("Connection", "keep-alive");
                                    put("Content-Length", "59");
                                    put("Content-Type", "application/x-www-form-urlencoded");
                                    put("Accept", "*/*");
                                }}
                        ))),
                        ""
                ), Arguments.of(
                        "확장자가 없는데 쿼리스트링이 있는 path",
                        new Request(new GetMessage(RequestHeader.of(
                                Arrays.asList(
                                        "GET",
                                        "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net",
                                        "HTTP/1.1"
                                ),
                                new HashMap() {{
                                    put("Host", "localhost:8080");
                                    put("Connection", "keep-alive");
                                    put("Content-Length", "59");
                                    put("Content-Type", "application/x-www-form-urlencoded");
                                    put("Accept", "*/*");
                                }}
                        ))),
                        ""
                )
        );
    }
}
