package webserver.http.header;

import webserver.http.attribute.Attributes;
import webserver.http.startline.RequestLine;

import java.util.List;

public class RequestHeader extends Header {
    private RequestLine statusLine;

    protected RequestHeader(RequestLine statusLine, Attributes attributes) {
        super(attributes);
        this.statusLine = statusLine;
    }

    public static RequestHeader of(List<String> statusLine, Attributes attributes) {
        return new RequestHeader(RequestLine.from(statusLine), attributes);
    }

    public static RequestHeader from(String headerText) {
        return RequestHeader.of(parseStartLine(headerText), Attributes.from(headerText));
    }

    public String getPath() {
        return statusLine.getPath();
    }

    public String getMethod() {
        return statusLine.getMethod();
    }

    @Override
    protected String getStartLine() {
        return statusLine.toString();
    }

    public String getQueryString() {
        return statusLine.getQueryString();
    }
}
