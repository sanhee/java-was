package webserver;

import db.DataBase;
import model.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class RequestHandlerTest {
    @ParameterizedTest
    @MethodSource("run")
    void run(String message, String expectedResponseMessage) throws IOException {
        int port = ThreadLocalRandom.current().nextInt(50000, 60000);
        ServerSocket server = new ServerSocket(port);

        Socket browser = new Socket("localhost", port);

        RequestHandler requestHandler = new RequestHandler(server.accept());

        BufferedOutputStream browserStream = new BufferedOutputStream(browser.getOutputStream());


        browserStream.write(message.getBytes(StandardCharsets.UTF_8));
        browserStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        browserStream.flush();

        requestHandler.run();

        BufferedReader br = new BufferedReader(new InputStreamReader(browser.getInputStream()));

        assertThat(br.lines().collect(Collectors.joining(System.lineSeparator()))).isEqualTo(expectedResponseMessage);
    }

    static Stream<Arguments> run() throws IOException {
        return Stream.of(
                Arguments.arguments(
                        "GET /index.html HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator(),
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: 6903" + System.lineSeparator() +
                                "" + System.lineSeparator() +
                                Files.lines(new File("./webapp/index.html").toPath())
                                        .collect(Collectors.joining(System.lineSeparator()))
                ), Arguments.arguments(
                        "GET /index2.html HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator(),
                        "HTTP/1.1 404 NotFound" + System.lineSeparator()
                ), Arguments.arguments(
                        "GET /user/form.html HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator(),
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: 5169" + System.lineSeparator() +
                                "" + System.lineSeparator() +
                                Files.lines(new File("./webapp/user/form.html").toPath())
                                        .collect(Collectors.joining(System.lineSeparator()))
                ), Arguments.arguments(
                        "POST /user/create HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 48" + System.lineSeparator() +
                                "Cache-Control: max-age=0" + System.lineSeparator() +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + System.lineSeparator() +
                                "sec-ch-ua-mobile: ?0" + System.lineSeparator() +
                                "Upgrade-Insecure-Requests: 1" + System.lineSeparator() +
                                "Origin: http://localhost:8080" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + System.lineSeparator() +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + System.lineSeparator() +
                                "Sec-Fetch-Site: same-origin" + System.lineSeparator() +
                                "Sec-Fetch-Mode: navigate" + System.lineSeparator() +
                                "Sec-Fetch-User: ?1" + System.lineSeparator() +
                                "Sec-Fetch-Dest: document" + System.lineSeparator() +
                                "Referer: http://localhost:8080/user/form.html" + System.lineSeparator() +
                                "Accept-Encoding: gzip, deflate, br" + System.lineSeparator() +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + System.lineSeparator() +
                                "Cookie: _ga=GA1.1.773336800.1611186274; Idea-dc7ca9b6=ac856d6e-e872-46ac-b153-000bdad105ec" + System.lineSeparator() +
                                "" + System.lineSeparator() +
                                "userId=dae&password=dae&name=dae&email=dae%40dae",
                        "HTTP/1.1 302 Found" + System.lineSeparator() +
                                "Location: http://localhost:8080/index.html" + System.lineSeparator()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("runWithCheckUserCreated")
    void runWithCheckUserCreated(String message, User expectedUser) throws IOException {
        int port = ThreadLocalRandom.current().nextInt(50000, 60000);
        ServerSocket server = new ServerSocket(port);

        Socket browser = new Socket("localhost", port);

        RequestHandler requestHandler = new RequestHandler(server.accept());

        BufferedOutputStream browserStream = new BufferedOutputStream(browser.getOutputStream());


        browserStream.write(message.getBytes(StandardCharsets.UTF_8));
        browserStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        browserStream.flush();

        requestHandler.run();

        assertThat(DataBase.findUserById("test").get()).isEqualTo(expectedUser);
    }

    static Stream<Arguments> runWithCheckUserCreated() {
        return Stream.of(
                Arguments.arguments(
                        "POST /user/create HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 53" + System.lineSeparator() +
                                "Cache-Control: max-age=0" + System.lineSeparator() +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + System.lineSeparator() +
                                "sec-ch-ua-mobile: ?0" + System.lineSeparator() +
                                "Upgrade-Insecure-Requests: 1" + System.lineSeparator() +
                                "Origin: http://localhost:8080" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + System.lineSeparator() +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + System.lineSeparator() +
                                "Sec-Fetch-Site: same-origin" + System.lineSeparator() +
                                "Sec-Fetch-Mode: navigate" + System.lineSeparator() +
                                "Sec-Fetch-User: ?1" + System.lineSeparator() +
                                "Sec-Fetch-Dest: document" + System.lineSeparator() +
                                "Referer: http://localhost:8080/user/form.html" + System.lineSeparator() +
                                "Accept-Encoding: gzip, deflate, br" + System.lineSeparator() +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + System.lineSeparator() +
                                "Cookie: _ga=GA1.1.773336800.1611186274; Idea-dc7ca9b6=ac856d6e-e872-46ac-b153-000bdad105ec" + System.lineSeparator() +
                                "" + System.lineSeparator() +
                                "userId=test&password=test&name=test&email=test%40test",
                        User.builder()
                                .setUserId("test")
                                .setPassword("test")
                                .setName("test")
                                .setEmail("test@test")
                                .build()
                ), Arguments.arguments(
                        "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator(),
                        User.builder()
                                .setUserId("test")
                                .setPassword("1234")
                                .setName("test")
                                .setEmail("test@test")
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("runWithFailToCreateUser")
    void runWithFailToCreateUser(String message) throws IOException {
        int port = ThreadLocalRandom.current().nextInt(50000, 60000);
        ServerSocket server = new ServerSocket(port);

        Socket browser = new Socket("localhost", port);

        RequestHandler requestHandler = new RequestHandler(server.accept());

        BufferedOutputStream browserStream = new BufferedOutputStream(browser.getOutputStream());


        browserStream.write(message.getBytes(StandardCharsets.UTF_8));
        browserStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        browserStream.flush();


        assertThatIllegalArgumentException()
                .isThrownBy(requestHandler::run);
    }

    static Stream<Arguments> runWithFailToCreateUser() {
        return Stream.of(
                Arguments.arguments(
                        "POST /user/create HTTP/1.1" + System.lineSeparator() +
                                "Content-Length: 3" + System.lineSeparator() +
                                System.lineSeparator() +
                                "a=b"
                ));
    }

    @ParameterizedTest
    @MethodSource("runWithLogin")
    void runWithLogin(String desc, User user, String message, String expectedResponseMessage) throws IOException {
        // 회원가입
        DataBase.addUser(user);

        // 로그인
        int port = ThreadLocalRandom.current().nextInt(30000, 60000);
        ServerSocket server = new ServerSocket(port);
        Socket browser = new Socket("localhost", port);
        RequestHandler requestHandler = new RequestHandler(server.accept());

        BufferedOutputStream browserStream = new BufferedOutputStream(browser.getOutputStream());

        browserStream.write(message.getBytes(StandardCharsets.UTF_8));
        browserStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        browserStream.flush();

        requestHandler.run();

        BufferedReader br = new BufferedReader(new InputStreamReader(browser.getInputStream()));
        String actualResponseMessage = br.lines().collect(Collectors.joining(System.lineSeparator()));

        // index.html로 이동
        assertThat(actualResponseMessage)
                .as("runWithLogin : %s", desc)
                .isEqualTo(expectedResponseMessage);
    }

    static Stream<Arguments> runWithLogin() throws IOException {
        return Stream.of(
                Arguments.arguments(
                        "로그인 성공",
                        User.builder()
                                .setUserId("test")
                                .setPassword("test")
                                .setName("test")
                                .setEmail("test@test")
                                .build(),
                        "POST /user/login HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 53" + System.lineSeparator() +
                                "Cache-Control: max-age=0" + System.lineSeparator() +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + System.lineSeparator() +
                                "sec-ch-ua-mobile: ?0" + System.lineSeparator() +
                                "Upgrade-Insecure-Requests: 1" + System.lineSeparator() +
                                "Origin: http://localhost:8080" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + System.lineSeparator() +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + System.lineSeparator() +
                                "Sec-Fetch-Site: same-origin" + System.lineSeparator() +
                                "Sec-Fetch-Mode: navigate" + System.lineSeparator() +
                                "Sec-Fetch-User: ?1" + System.lineSeparator() +
                                "Sec-Fetch-Dest: document" + System.lineSeparator() +
                                "Referer: http://localhost:8080/user/form.html" + System.lineSeparator() +
                                "Accept-Encoding: gzip, deflate, br" + System.lineSeparator() +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + System.lineSeparator() +
                                "Cookie: _ga=GA1.1.773336800.1611186274; Idea-dc7ca9b6=ac856d6e-e872-46ac-b153-000bdad105ec" + System.lineSeparator() +
                                "" + System.lineSeparator() +
                                "userId=test&password=test&name=test&email=test%40test",
                        "HTTP/1.1 302 Found" + System.lineSeparator() +
                                "Location: http://localhost:8080/index.html" + System.lineSeparator() +
                                "Set-Cookie: logined=true; Path=/" + System.lineSeparator()
                ), Arguments.arguments(
                        "로그인 실패 - 잘못된 아이디",
                        User.builder()
                                .setUserId("test")
                                .setPassword("test")
                                .setName("test")
                                .setEmail("test@test")
                                .build(),
                        "POST /user/login HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 53" + System.lineSeparator() +
                                "Cache-Control: max-age=0" + System.lineSeparator() +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + System.lineSeparator() +
                                "sec-ch-ua-mobile: ?0" + System.lineSeparator() +
                                "Upgrade-Insecure-Requests: 1" + System.lineSeparator() +
                                "Origin: http://localhost:8080" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + System.lineSeparator() +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + System.lineSeparator() +
                                "Sec-Fetch-Site: same-origin" + System.lineSeparator() +
                                "Sec-Fetch-Mode: navigate" + System.lineSeparator() +
                                "Sec-Fetch-User: ?1" + System.lineSeparator() +
                                "Sec-Fetch-Dest: document" + System.lineSeparator() +
                                "Referer: http://localhost:8080/user/form.html" + System.lineSeparator() +
                                "Accept-Encoding: gzip, deflate, br" + System.lineSeparator() +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + System.lineSeparator() +
                                "Cookie: _ga=GA1.1.773336800.1611186274; Idea-dc7ca9b6=ac856d6e-e872-46ac-b153-000bdad105ec" + System.lineSeparator() +
                                "" + System.lineSeparator() +
                                "userId=wrongId&password=test&name=test&email=test%40test",
                        "HTTP/1.1 302 Found" + System.lineSeparator() +
                                "Location: http://localhost:8080/user/login_failed.html" + System.lineSeparator()
                ), Arguments.arguments(
                        "로그인 실패 - 잘못된 비밀번호",
                        User.builder()
                                .setUserId("test")
                                .setPassword("test")
                                .setName("test")
                                .setEmail("test@test")
                                .build(),
                        "POST /user/login HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Content-Length: 53" + System.lineSeparator() +
                                "Cache-Control: max-age=0" + System.lineSeparator() +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + System.lineSeparator() +
                                "sec-ch-ua-mobile: ?0" + System.lineSeparator() +
                                "Upgrade-Insecure-Requests: 1" + System.lineSeparator() +
                                "Origin: http://localhost:8080" + System.lineSeparator() +
                                "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + System.lineSeparator() +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + System.lineSeparator() +
                                "Sec-Fetch-Site: same-origin" + System.lineSeparator() +
                                "Sec-Fetch-Mode: navigate" + System.lineSeparator() +
                                "Sec-Fetch-User: ?1" + System.lineSeparator() +
                                "Sec-Fetch-Dest: document" + System.lineSeparator() +
                                "Referer: http://localhost:8080/user/form.html" + System.lineSeparator() +
                                "Accept-Encoding: gzip, deflate, br" + System.lineSeparator() +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + System.lineSeparator() +
                                "Cookie: _ga=GA1.1.773336800.1611186274; Idea-dc7ca9b6=ac856d6e-e872-46ac-b153-000bdad105ec" + System.lineSeparator() +
                                "" + System.lineSeparator() +
                                "userId=test&password=wrongPw&name=test&email=test%40test",
                        "HTTP/1.1 302 Found" + System.lineSeparator() +
                                "Location: http://localhost:8080/user/login_failed.html" + System.lineSeparator()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("loginHandler")
    void loginHandler(String desc, User user, Map<String, String> parameters, String expectedResponseMessage) {
        DataBase.addUser(user);
        RequestHandler requestHandler = new RequestHandler(new Socket());

        String actualResponseMessage = requestHandler.loginHandler(parameters);

        assertThat(actualResponseMessage).isEqualTo(expectedResponseMessage);
    }

    static Stream<Arguments> loginHandler() {
        return Stream.of(
                Arguments.arguments(
                        "로그인 성공",
                        User.builder()
                                .setUserId("test")
                                .setPassword("test")
                                .setName("test")
                                .setEmail("test@test")
                                .build(),
                        new HashMap() {{
                            put("userId", "test");
                            put("password", "test");
                            put("name", "test");
                            put("email", "test@test");
                        }},
                        "HTTP/1.1 302 Found" + System.lineSeparator() +
                                "Location: http://localhost:8080/index.html" + System.lineSeparator() +
                                "Set-Cookie: logined=true; Path=/"
                ), Arguments.arguments(
                        "로그인 실패 - 잘못된 ID",
                        User.builder()
                                .setUserId("test")
                                .setPassword("test")
                                .setName("test")
                                .setEmail("test@test")
                                .build(),
                        new HashMap() {{
                            put("userId", "wrongId");
                            put("password", "test");
                            put("name", "test");
                            put("email", "test@test");
                        }},
                        "HTTP/1.1 302 Found" + System.lineSeparator() +
                                "Location: http://localhost:8080/user/login_failed.html"
                ), Arguments.arguments(
                        "로그인 실패 - 잘못된 비밀번호",
                        User.builder()
                                .setUserId("test")
                                .setPassword("test")
                                .setName("test")
                                .setEmail("test@test")
                                .build(),
                        new HashMap() {{
                            put("userId", "test");
                            put("password", "wrongPassword");
                            put("name", "test");
                            put("email", "test@test");
                        }},
                        "HTTP/1.1 302 Found" + System.lineSeparator() +
                                "Location: http://localhost:8080/user/login_failed.html"
                )
        );
    }
}
