package webserver.http.message;

import webserver.Const;
import webserver.http.Body;
import webserver.http.header.RequestHeader;
import webserver.http.startline.RequestLine;

import java.util.Map;

import static util.HttpRequestUtils.extractStartLineFrom;


public interface RequestMessage {
    static RequestMessage from(String message) {
        String[] splitMessage = message.split(Const.CRLF + Const.CRLF);
        RequestLine requestLine = RequestLine.from(extractStartLineFrom(splitMessage[0]));
        RequestHeader header = RequestHeader.from(splitMessage[0]);

        if (requestLine.getMethod().equalsIgnoreCase("post")) {
            Body body = Body.from(splitMessage[1]);
            return new PostMessage(requestLine, header, body);
        }

        return new GetMessage(requestLine, header);
    }

    RequestHeader getHeader();

    String getMethod();

    Map<String, String> getParameters();

    String getPath();

    RequestLine getRequestLine();
}
