package webserver.http.header;

import util.HttpRequestUtils;
import webserver.http.statusline.RequestStatusLine;

import java.util.List;
import java.util.Map;

public class RequestHeader extends Header {
    private RequestStatusLine statusLine;

    protected RequestHeader(RequestStatusLine statusLine, Map<String, String> attributes) {
        super(attributes);
        this.statusLine = statusLine;
    }

    public static RequestHeader of(List<String> statusLine, Map<String, String> attributes) {
        return new RequestHeader(RequestStatusLine.from(statusLine), attributes);
    }

    public static RequestHeader from(String headerText) {
        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        List<String> statusLine = HttpRequestUtils.parseStatusLine(splittedHeaderTexts[0]);

        return RequestHeader.of(statusLine, attributeFrom(headerText));
    }

    public String path() {
        return statusLine.path();
    }

    public String getMethod() {
        return statusLine.method();
    }

    @Override
    protected String statusLine() {
        return statusLine.toString();
    }

    public String queryString() {
        return statusLine.queryString();
    }

    public String pathExtension() {
        return statusLine.pathExtension();
    }
}
