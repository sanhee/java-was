package webserver.http.header;

import util.HttpRequestUtils;
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
        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        List<String> statusLine = HttpRequestUtils.parseStatusLine(splittedHeaderTexts[0]);

        return RequestHeader.of(statusLine, Attributes.from(headerText));
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

    public int getContentLength() {
        return Integer.parseInt(super.getAttributes().getOrDefault("Content-Length", "0"));
    }
}
