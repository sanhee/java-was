package webserver;

import db.DataBase;
import model.PasswordNotMatchException;
import model.User;
import model.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import webserver.http.Request;
import webserver.http.Response;
import webserver.http.header.RequestHeader;

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

            RequestHeader requestHeader = RequestHeader.from(requestMessages.toString());

            String requestBody = IOUtils.readData(br, requestHeader.getContentLength());

            requestMessages.add(System.lineSeparator() + requestBody);

            Request request = Request.from(requestMessages.toString());

            // TODO: Default Message를 설정할 수 있을 것 같다.
            String responseMessage = "HTTP/1.1 404 NotFound" + System.lineSeparator() + System.lineSeparator();

            String path = request.getPath();
            String extension = request.getPathExtension();
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

            switch (path) {
                case "/user/create": {
                    Map<String, String> parameters = request.getRequestMessage().getParameters();

                    User newUser = User.builder()
                            .setUserId(parameters.get("userId"))
                            .setPassword(parameters.get("password"))
                            .setName(parameters.get("name"))
                            .setEmail(parameters.get("email"))
                            .build();

                    DataBase.addUser(newUser);

                    responseMessage = "HTTP/1.1 302 Found" + System.lineSeparator() +
                            "Location: /index.html";
                    break;
                }
                case "/user/login": {
                    responseMessage = loginHandler(request.getRequestMessage().getParameters());
                }
            }

            Response response = Response.from(responseMessage);
            response.write(out);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public String loginHandler(Map<String, String> parameters) {
        try {
            User findUser;

            try {
                findUser = DataBase.findUserById(parameters.get("userId"))
                        .orElseThrow(UserNotFoundException::new);
            } catch (UserNotFoundException userNotFoundException) {
                throw new LoginFailedException();
            }

            try {
                findUser.checkPassword(parameters.get("password"));
            } catch (PasswordNotMatchException passwordNotMatchException) {
                throw new LoginFailedException();
            }

            return "HTTP/1.1 302 Found" + System.lineSeparator() +
                    "Location: /index.html" + System.lineSeparator() +
                    "Set-Cookie: logined=true; Path=/";
        } catch (LoginFailedException loginFailedException) {
            return "HTTP/1.1 302 Found" + System.lineSeparator() +
                    "Location: /user/login_failed.html" + System.lineSeparator() +
                    "Set-Cookie: logined=false; Path=/";
        }
    }
}
