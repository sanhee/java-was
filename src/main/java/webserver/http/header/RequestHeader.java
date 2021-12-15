package webserver.http.header;

import webserver.http.attribute.Attributes;
import webserver.http.startline.RequestLine;

import java.util.List;

public class RequestHeader extends Header {
    private RequestLine requestLine;

    protected RequestHeader(RequestLine requestLine, Attributes attributes) {
        super(attributes);
        this.requestLine = requestLine;
    }

    public static RequestHeader of(List<String> statusLine, Attributes attributes) {
        return new RequestHeader(RequestLine.from(statusLine), attributes);
    }

    public static RequestHeader from(String headerText) {
        return RequestHeader.of(parseStartLine(headerText), Attributes.from(headerText));
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getMethod() {
        return requestLine.getMethod();
    }

    @Override
    protected String getStartLine() {
        return requestLine.toString();
    }

    public String getQueryString() {
        return requestLine.getQueryString();
    }
}
