package webserver.http.header;

import util.HttpRequestUtils;
import webserver.http.Attribute;
import webserver.http.statusline.ResponseStatusLine;

import java.util.List;
import java.util.Map;

public class ResponseHeader extends Header {

    private ResponseStatusLine statusLine;

    protected ResponseHeader(ResponseStatusLine statusLine, Map<String, String> attributes) {
        super(attributes);
        this.statusLine = statusLine;
    }

    protected ResponseHeader(ResponseStatusLine statusLine, Attribute attributes) {
        super(attributes);
        this.statusLine = statusLine;
    }

    public static ResponseHeader of(List<String> statusLine, Map<String, String> attributes) {
        return new ResponseHeader(ResponseStatusLine.from(statusLine), attributes);
    }

    public static ResponseHeader of(List<String> statusLine, Attribute attributes) {
        return new ResponseHeader(ResponseStatusLine.from(statusLine), attributes);
    }

    public static ResponseHeader from(String headerText) {
        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        List<String> statusLine = HttpRequestUtils.parseStatusLine(splittedHeaderTexts[0]);

        return ResponseHeader.of(statusLine, Attribute.attributeFrom(headerText));
    }

    public static ResponseHeader newFrom(String headerText) {
        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        List<String> statusLine = HttpRequestUtils.parseStatusLine(splittedHeaderTexts[0]);

        return ResponseHeader.of(statusLine, Attribute.attributeNewFrom(headerText));
    }

    @Override
    protected String getStatusLine() {
        return statusLine.toString();
    }
}
