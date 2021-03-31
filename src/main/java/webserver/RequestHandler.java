package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

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

            Request request = Request.from(in);
            //TODO: StatusLine 대신 path에서 바로 뽑을 수 있을 것 같다.
            Map<String, String> statusLine = request.getRequestMessage().getHeader().getStatusLineAttributes();
            String path = statusLine.get(RequestHeader.PATH_KEY);

            // TODO: Default Message를 설정할 수 있을 것 같다.
            String message = "HTTP/1.1 404 NotFound" + System.lineSeparator() + System.lineSeparator();

            if (path.equals("/index.html")) {
                message = "HTTP/1.1 200 OK" + System.lineSeparator() +
                        "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                        "Content-Length: " + "Hello World".getBytes().length + System.lineSeparator() +
                        System.lineSeparator() +
                        "Hello World";
            }

            Response response = Response.from(message);
            response.write(out);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
