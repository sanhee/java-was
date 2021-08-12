package webserver.http.message;

import webserver.http.header.Header;
import webserver.http.header.RequestHeader;

import java.util.Map;

public interface RequestMessage {
    static RequestMessage from(String message) {
        RequestHeader header = Header.requestHeaderFrom(message.split(System.lineSeparator())[0]);

        if (header.getMethod().equalsIgnoreCase("post")) {
            return PostMessage.from(message);
        }
        
        return GetMessage.from(message);
    }

    RequestHeader getHeader();

    String getMethod();

    Map<String, String> getParameters();
}
