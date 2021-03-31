package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private RequestMessage requestMessage;

    private Request(RequestMessage requestMessage) {
        this.requestMessage = requestMessage;
    }

    public static Request from(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder messageSb = new StringBuilder();

        String message = br.readLine();
        messageSb.append(message).append(System.lineSeparator());

        while (message.length() != 0) {
            message = br.readLine();
            messageSb.append(message).append(System.lineSeparator());
        }

        return new Request(RequestMessage.from(messageSb.toString()));
    }

    public RequestMessage getRequestMessage() {
        return requestMessage;
    }
}
