package webserver.http.header;

import util.HttpRequestUtils;
import webserver.http.Attribute;
import webserver.http.statusline.RequestStatusLine;

import java.util.List;
import java.util.Map;

public class RequestHeader extends Header {
    private RequestStatusLine statusLine;

    protected RequestHeader(RequestStatusLine statusLine, Map<String, String> attributes) {
        super(attributes);
        this.statusLine = statusLine;
    }

    protected RequestHeader(RequestStatusLine statusLine, Attribute attributes) {
        super(attributes);
        this.statusLine = statusLine;
    }

    public static RequestHeader of(List<String> statusLine, Map<String, String> attributes) {
        return new RequestHeader(RequestStatusLine.from(statusLine), attributes);
    }

    public static RequestHeader of(List<String> statusLine, Attribute attributes) {
        return new RequestHeader(RequestStatusLine.from(statusLine), attributes);
    }

    public static RequestHeader from(String headerText) {
        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        List<String> statusLine = HttpRequestUtils.parseStatusLine(splittedHeaderTexts[0]);

        return RequestHeader.of(statusLine, attributeFrom(headerText));
    }

    public static RequestHeader newFrom(String headerText) {
        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        List<String> statusLine = HttpRequestUtils.parseStatusLine(splittedHeaderTexts[0]);

        return RequestHeader.of(statusLine, attributeNewFrom(headerText));
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
