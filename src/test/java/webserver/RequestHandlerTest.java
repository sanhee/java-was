package webserver;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
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
import static webserver.FileForTest.FORM_HTML;
import static webserver.FileForTest.INDEX_HTML;

class RequestHandlerTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    ServerSocket server;
    Socket browser;

    @BeforeEach
    void setUp() throws IOException {
        setUpServer();
        cleanUpDataBase();
    }

    private void setUpServer() throws IOException {
        server = createServerWithRandomPortBetween(30000, 40000);
        browser = new Socket("localhost", server.getLocalPort());
    }

    private void cleanUpDataBase() {
        DataBase.deleteAll();
    }

    @AfterEach
    void tearDown() throws IOException {
        server.close();
        browser.close();
    }

    private ServerSocket createServerWithRandomPortBetween(int origin, int bound) throws IOException {
        int port = ThreadLocalRandom.current().nextInt(origin, bound);

        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
        } catch (BindException bindException) {
            logger.debug("port [" + port + "] already exists", bindException);
            return createServerWithRandomPortBetween(origin, bound);
        }

        return server;
    }

    @ParameterizedTest
    @MethodSource("run")
    void run(String message, String expectedResponseMessage) throws IOException {
        RequestHandler requestHandler = new RequestHandler(server.accept());

        BufferedOutputStream browserStream = new BufferedOutputStream(browser.getOutputStream());


        browserStream.write(message.getBytes(StandardCharsets.UTF_8));
        browserStream.write(Const.CRLF.getBytes(StandardCharsets.UTF_8));
        browserStream.flush();

        requestHandler.run();

        BufferedReader br = new BufferedReader(new InputStreamReader(browser.getInputStream()));

        assertThat(br.lines().collect(Collectors.joining(System.lineSeparator()))).isEqualTo(expectedResponseMessage);
    }

    static Stream<Arguments> run() throws IOException {
        return Stream.of(
                Arguments.arguments(
                        "GET /index.html HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Accept: */*" + Const.CRLF +
                                "" + Const.CRLF,
                        "HTTP/1.1 200 OK" + Const.CRLF +
                                "Content-Type: text/html;charset=utf-8" + Const.CRLF +
                                "Content-Length: " + INDEX_HTML.length() + Const.CRLF +
                                "" + Const.CRLF +
                                Files.lines(INDEX_HTML.toPath())
                                        .collect(Collectors.joining(System.lineSeparator()))
                ), Arguments.arguments(
                        "GET /index2.html HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Accept: */*" + Const.CRLF +
                                "" + Const.CRLF,
                        "HTTP/1.1 404 NotFound" + Const.CRLF
                ), Arguments.arguments(
                        "GET /user/form.html HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Accept: */*" + Const.CRLF +
                                "" + Const.CRLF,
                        "HTTP/1.1 200 OK" + Const.CRLF +
                                "Content-Type: text/html;charset=utf-8" + Const.CRLF +
                                "Content-Length: " + FORM_HTML.length() + Const.CRLF +
                                "" + Const.CRLF +
                                Files.lines(FORM_HTML.toPath())
                                        .collect(Collectors.joining(System.lineSeparator()))
                ), Arguments.arguments(
                        "POST /user/create HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Content-Length: 48" + Const.CRLF +
                                "Cache-Control: max-age=0" + Const.CRLF +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + Const.CRLF +
                                "sec-ch-ua-mobile: ?0" + Const.CRLF +
                                "Upgrade-Insecure-Requests: 1" + Const.CRLF +
                                "Origin: http://localhost:8080" + Const.CRLF +
                                "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + Const.CRLF +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + Const.CRLF +
                                "Sec-Fetch-Site: same-origin" + Const.CRLF +
                                "Sec-Fetch-Mode: navigate" + Const.CRLF +
                                "Sec-Fetch-User: ?1" + Const.CRLF +
                                "Sec-Fetch-Dest: document" + Const.CRLF +
                                "Referer: http://localhost:8080/user/form.html" + Const.CRLF +
                                "Accept-Encoding: gzip, deflate, br" + Const.CRLF +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + Const.CRLF +
                                "Cookie: _ga=GA1.1.773336800.1611186274; Idea-dc7ca9b6=ac856d6e-e872-46ac-b153-000bdad105ec" + Const.CRLF +
                                "" + Const.CRLF +
                                "userId=dae&password=dae&name=dae&email=dae%40dae",
                        "HTTP/1.1 302 Found" + Const.CRLF +
                                "Location: /index.html" + Const.CRLF
                )
        );
    }

    @ParameterizedTest
    @MethodSource("runWithCheckUserCreated")
    void runWithCheckUserCreated(String message, User expectedUser) throws IOException {
        RequestHandler requestHandler = new RequestHandler(server.accept());

        BufferedOutputStream browserStream = new BufferedOutputStream(browser.getOutputStream());


        browserStream.write(message.getBytes(StandardCharsets.UTF_8));
        browserStream.write(Const.CRLF.getBytes(StandardCharsets.UTF_8));
        browserStream.flush();

        requestHandler.run();

        assertThat(DataBase.findUserById(expectedUser.getUserId()).get()).isEqualTo(expectedUser);
    }

    static Stream<Arguments> runWithCheckUserCreated() {
        return Stream.of(
                Arguments.arguments(
                        "POST /user/create HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Content-Length: 53" + Const.CRLF +
                                "Cache-Control: max-age=0" + Const.CRLF +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + Const.CRLF +
                                "sec-ch-ua-mobile: ?0" + Const.CRLF +
                                "Upgrade-Insecure-Requests: 1" + Const.CRLF +
                                "Origin: http://localhost:8080" + Const.CRLF +
                                "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + Const.CRLF +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + Const.CRLF +
                                "Sec-Fetch-Site: same-origin" + Const.CRLF +
                                "Sec-Fetch-Mode: navigate" + Const.CRLF +
                                "Sec-Fetch-User: ?1" + Const.CRLF +
                                "Sec-Fetch-Dest: document" + Const.CRLF +
                                "Referer: http://localhost:8080/user/form.html" + Const.CRLF +
                                "Accept-Encoding: gzip, deflate, br" + Const.CRLF +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + Const.CRLF +
                                "Cookie: _ga=GA1.1.773336800.1611186274; Idea-dc7ca9b6=ac856d6e-e872-46ac-b153-000bdad105ec" + Const.CRLF +
                                "" + Const.CRLF +
                                "userId=test&password=test&name=test&email=test%40test",
                        User.builder()
                                .setUserId("test")
                                .setPassword("test")
                                .setName("test")
                                .setEmail("test@test")
                                .build()
                ), Arguments.arguments(
                        "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Accept: */*" + Const.CRLF,
                        User.builder()
                                .setUserId("javajigi")
                                .setPassword("password")
                                .setName("test")
                                .setEmail("javajigi@40slipp.net")
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("runWithFailToCreateUser")
    void runWithFailToCreateUser(String message) throws IOException {
        RequestHandler requestHandler = new RequestHandler(server.accept());

        BufferedOutputStream browserStream = new BufferedOutputStream(browser.getOutputStream());


        browserStream.write(message.getBytes(StandardCharsets.UTF_8));
        browserStream.write(Const.CRLF.getBytes(StandardCharsets.UTF_8));
        browserStream.flush();


        assertThatIllegalArgumentException()
                .isThrownBy(requestHandler::run);
    }

    static Stream<Arguments> runWithFailToCreateUser() {
        return Stream.of(
                Arguments.arguments(
                        "POST /user/create HTTP/1.1" + Const.CRLF +
                                "Content-Length: 3" + Const.CRLF +
                                Const.CRLF +
                                "a=b"
                ));
    }

    @ParameterizedTest
    @MethodSource("runWithLogin")
    void runWithLogin(String desc, User user, String message, String expectedResponseMessage) throws IOException {
        // 회원가입
        DataBase.addUser(user);

        // 로그인
        RequestHandler requestHandler = new RequestHandler(server.accept());

        BufferedOutputStream browserStream = new BufferedOutputStream(browser.getOutputStream());

        browserStream.write(message.getBytes(StandardCharsets.UTF_8));
        browserStream.write(Const.CRLF.getBytes(StandardCharsets.UTF_8));
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
                        "POST /user/login HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Content-Length: 53" + Const.CRLF +
                                "Cache-Control: max-age=0" + Const.CRLF +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + Const.CRLF +
                                "sec-ch-ua-mobile: ?0" + Const.CRLF +
                                "Upgrade-Insecure-Requests: 1" + Const.CRLF +
                                "Origin: http://localhost:8080" + Const.CRLF +
                                "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + Const.CRLF +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + Const.CRLF +
                                "Sec-Fetch-Site: same-origin" + Const.CRLF +
                                "Sec-Fetch-Mode: navigate" + Const.CRLF +
                                "Sec-Fetch-User: ?1" + Const.CRLF +
                                "Sec-Fetch-Dest: document" + Const.CRLF +
                                "Referer: http://localhost:8080/user/form.html" + Const.CRLF +
                                "Accept-Encoding: gzip, deflate, br" + Const.CRLF +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + Const.CRLF +
                                "Cookie: _ga=GA1.1.773336800.1611186274; Idea-dc7ca9b6=ac856d6e-e872-46ac-b153-000bdad105ec" + Const.CRLF +
                                "" + Const.CRLF +
                                "userId=test&password=test&name=test&email=test%40test",
                        "HTTP/1.1 302 Found" + Const.CRLF +
                                "Location: /index.html" + Const.CRLF +
                                "Set-Cookie: logined=true; Path=/" + Const.CRLF
                ), Arguments.arguments(
                        "로그인 실패 - 잘못된 아이디",
                        User.builder()
                                .setUserId("test")
                                .setPassword("test")
                                .setName("test")
                                .setEmail("test@test")
                                .build(),
                        "POST /user/login HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Content-Length: 53" + Const.CRLF +
                                "Cache-Control: max-age=0" + Const.CRLF +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + Const.CRLF +
                                "sec-ch-ua-mobile: ?0" + Const.CRLF +
                                "Upgrade-Insecure-Requests: 1" + Const.CRLF +
                                "Origin: http://localhost:8080" + Const.CRLF +
                                "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + Const.CRLF +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + Const.CRLF +
                                "Sec-Fetch-Site: same-origin" + Const.CRLF +
                                "Sec-Fetch-Mode: navigate" + Const.CRLF +
                                "Sec-Fetch-User: ?1" + Const.CRLF +
                                "Sec-Fetch-Dest: document" + Const.CRLF +
                                "Referer: http://localhost:8080/user/form.html" + Const.CRLF +
                                "Accept-Encoding: gzip, deflate, br" + Const.CRLF +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + Const.CRLF +
                                "Cookie: _ga=GA1.1.773336800.1611186274; Idea-dc7ca9b6=ac856d6e-e872-46ac-b153-000bdad105ec" + Const.CRLF +
                                "" + Const.CRLF +
                                "userId=wrongId&password=test&name=test&email=test%40test",
                        "HTTP/1.1 302 Found" + Const.CRLF +
                                "Location: /user/login_failed.html" + Const.CRLF +
                                "Set-Cookie: logined=false; Path=/" + Const.CRLF
                ), Arguments.arguments(
                        "로그인 실패 - 잘못된 비밀번호",
                        User.builder()
                                .setUserId("test")
                                .setPassword("test")
                                .setName("test")
                                .setEmail("test@test")
                                .build(),
                        "POST /user/login HTTP/1.1" + Const.CRLF +
                                "Host: localhost:8080" + Const.CRLF +
                                "Connection: keep-alive" + Const.CRLF +
                                "Content-Length: 53" + Const.CRLF +
                                "Cache-Control: max-age=0" + Const.CRLF +
                                "sec-ch-ua: \"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"" + Const.CRLF +
                                "sec-ch-ua-mobile: ?0" + Const.CRLF +
                                "Upgrade-Insecure-Requests: 1" + Const.CRLF +
                                "Origin: http://localhost:8080" + Const.CRLF +
                                "Content-Type: application/x-www-form-urlencoded" + Const.CRLF +
                                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36" + Const.CRLF +
                                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9" + Const.CRLF +
                                "Sec-Fetch-Site: same-origin" + Const.CRLF +
                                "Sec-Fetch-Mode: navigate" + Const.CRLF +
                                "Sec-Fetch-User: ?1" + Const.CRLF +
                                "Sec-Fetch-Dest: document" + Const.CRLF +
                                "Referer: http://localhost:8080/user/form.html" + Const.CRLF +
                                "Accept-Encoding: gzip, deflate, br" + Const.CRLF +
                                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7" + Const.CRLF +
                                "Cookie: _ga=GA1.1.773336800.1611186274; Idea-dc7ca9b6=ac856d6e-e872-46ac-b153-000bdad105ec" + Const.CRLF +
                                "" + Const.CRLF +
                                "userId=test&password=wrongPw&name=test&email=test%40test",
                        "HTTP/1.1 302 Found" + Const.CRLF +
                                "Location: /user/login_failed.html" + Const.CRLF +
                                "Set-Cookie: logined=false; Path=/" + Const.CRLF
                )
        );
    }

    @ParameterizedTest
    @MethodSource("loginHandler")
    void loginHandler(String desc, User user, Map<String, String> parameters, String expectedResponseMessage) {
        DataBase.addUser(user);
        RequestHandler requestHandler = new RequestHandler(new Socket());

        String actualResponseMessage = requestHandler.loginHandler(parameters);

        assertThat(actualResponseMessage)
                .as("loginHandler : %s", desc)
                .isEqualTo(expectedResponseMessage);
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
                        "HTTP/1.1 302 Found" + Const.CRLF +
                                "Location: /index.html" + Const.CRLF +
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
                        "HTTP/1.1 302 Found" + Const.CRLF +
                                "Location: /user/login_failed.html" + Const.CRLF +
                                "Set-Cookie: logined=false; Path=/"
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
                        "HTTP/1.1 302 Found" + Const.CRLF +
                                "Location: /user/login_failed.html" + Const.CRLF +
                                "Set-Cookie: logined=false; Path=/"
                )
        );
    }
}
