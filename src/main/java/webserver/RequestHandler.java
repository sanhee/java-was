package webserver;

import db.DataBase;
import model.PasswordNotMatchException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;
import java.util.StringJoiner;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringJoiner requestMessages = new StringJoiner(System.lineSeparator());

            String requestMessage = br.readLine();

            while (requestMessage != null && requestMessage.length() != 0) {
                requestMessages.add(requestMessage);
                requestMessage = br.readLine();
            }

            RequestHeader requestHeader = Header.requestHeaderFrom(requestMessages.toString());

            int contentLength = Integer.parseInt(requestHeader.getAttributes().getOrDefault("Content-Length", "0"));
            String requestBody = IOUtils.readData(br, contentLength);

            requestMessages.add(System.lineSeparator() + requestBody);

            Request request = Request.from(requestMessages.toString());

            //TODO: StatusLine 대신 path에서 바로 뽑을 수 있을 것 같다.
            Map<String, String> statusLine = request.getRequestMessage().getHeader().getStatusLineAttributes();
            String path = statusLine.get(RequestHeader.PATH_KEY);

            // TODO: Default Message를 설정할 수 있을 것 같다.
            String responseMessage = "HTTP/1.1 404 NotFound" + System.lineSeparator() + System.lineSeparator();


            String[] splittedPath = path.split("/");

            String[] splittedDot = splittedPath[splittedPath.length - 1].split("\\.");

            String extension = "";

            if (1 < splittedDot.length) {
                extension = splittedPath[splittedPath.length - 1].split("\\.")[1];
            }

            if (extension.equals("html")) {

                File htmlFile = new File("./webapp" + path);

                if (htmlFile.exists()) {
                    byte[] body = Files.readAllBytes(htmlFile.toPath());

                    responseMessage = "HTTP/1.1 200 OK" + System.lineSeparator() +
                            "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                            "Content-Length: " + body.length + System.lineSeparator() +
                            System.lineSeparator() +
                            new String(body);
                }
            }

            if (path.equals("/user/create")) {
                Map<String, String> parameters = request.getRequestMessage().getParameters();

                User newUser = new User(
                        parameters.get("userId"),
                        parameters.get("password"),
                        parameters.get("name"),
                        parameters.get("email")
                );

                DataBase.addUser(newUser);

                responseMessage = "HTTP/1.1 302 Found" + System.lineSeparator() +
                        "Location: http://localhost:8080/index.html";
            }
            try {
                try {
                    if (path.equals("/user/login")) {
                        Map<String, String> parameters = request.getRequestMessage().getParameters();

                        User user = DataBase.findUserById(parameters.get("userId"));

                        if (user == null) {
                            throw new LoginFailedException();
                        }
                        user.checkPassword(parameters.get("password"));

                        responseMessage = "HTTP/1.1 302 Found" + System.lineSeparator() +
                                "Set-Cookie: logined=true; Path=/" + System.lineSeparator() +
                                "Location: http://localhost:8080/index.html";
                    }
                } catch (PasswordNotMatchException passwordNotMatchException) {
                    throw new LoginFailedException(passwordNotMatchException);
                }
            } catch (LoginFailedException loginFailedException) {
                log.debug("login failed", loginFailedException);

                responseMessage = "HTTP/1.1 302 Found" + System.lineSeparator() +
                        "Location: http://localhost:8080/login_failed.html";
            }

            Response response = Response.from(responseMessage);
            response.write(out);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
