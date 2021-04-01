package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            if (br.ready()) {
                requestMessage = br.readLine();

                while (requestMessage != null && requestMessage.length() != 0) {
                    requestMessages.add(requestMessage);
                    requestMessage = br.readLine();
                }
            }

            Request request = Request.from(requestMessages.toString());

            //TODO: StatusLine 대신 path에서 바로 뽑을 수 있을 것 같다.
            Map<String, String> statusLine = request.getRequestMessage().getHeader().getStatusLineAttributes();
            String path = statusLine.get(RequestHeader.PATH_KEY);

            // TODO: Default Message를 설정할 수 있을 것 같다.
            String responseMessage = "HTTP/1.1 404 NotFound" + System.lineSeparator() + System.lineSeparator();

            if (path.equals("/index.html")) {
                byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());

                responseMessage = "HTTP/1.1 200 OK" + System.lineSeparator() +
                        "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                        "Content-Length: " + body.length + System.lineSeparator() +
                        System.lineSeparator() +
                        new String(body);
            }

            Response response = Response.from(responseMessage);
            response.write(out);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
