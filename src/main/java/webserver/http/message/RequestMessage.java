package webserver.http.message;

import webserver.Const;
import webserver.http.Body;
import webserver.http.header.RequestHeader;
import webserver.http.startline.RequestLine;

import java.util.Map;

import static webserver.http.header.Header.parseStartLine;

public interface RequestMessage {
    static RequestMessage from(String message) {
        String[] splitMessage = message.split(Const.CRLF + Const.CRLF);
        RequestLine requestLine = RequestLine.from(parseStartLine(splitMessage[0]));
        RequestHeader header = RequestHeader.from(splitMessage[0]);

        if (header.getMethod().equalsIgnoreCase("post")) {
            return PostMessage.from(message);
        }

        return new GetMessage(requestLine, header);
    }

    RequestHeader getHeader();

    String getMethod();

    Map<String, String> getParameters();

    String getPath();
}
