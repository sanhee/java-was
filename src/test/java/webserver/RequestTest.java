package webserver;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RequestTest {

    @ParameterizedTest
    @MethodSource("from")
    void from(String inputMessage, RequestMessage expectedRequestMessage) {
        Request request = Request.from(inputMessage);
        //TODO: Path 체크 이후 Parameter 불러오는 곳 테스트 추가로 필요
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
                                // TODO: 쿼리스트링 저장소가 추가로 들어가야 함. 혹은 PATH를 가져올때 주소와 쿼리스트링 구분 필요
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
}
