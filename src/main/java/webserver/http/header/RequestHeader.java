package webserver.http.header;

import webserver.http.attribute.Attributes;
import webserver.http.statusline.RequestStatusLine;

import java.util.List;

public class RequestHeader extends Header {
    private RequestStatusLine statusLine;

    protected RequestHeader(RequestStatusLine statusLine, Attributes attributes) {
        super(attributes);
        this.statusLine = statusLine;
    }

    public static RequestHeader of(List<String> statusLine, Attributes attributes) {
        return new RequestHeader(RequestStatusLine.from(statusLine), attributes);
    }

    public static RequestHeader from(String headerText) {
        return RequestHeader.of(parseStatusLine(headerText), Attributes.from(headerText));
    }

    public String getPath() {
        return statusLine.getPath();
    }

    public String getMethod() {
        return statusLine.getMethod();
    }

    @Override
    protected String getStatusLine() {
        return statusLine.toString();
    }

    public String getQueryString() {
        return statusLine.getQueryString();
    }
}
