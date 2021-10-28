package webserver.http.header;

import util.HttpRequestUtils;
import webserver.http.Attribute;
import webserver.http.statusline.ResponseStatusLine;

import java.util.List;

public class ResponseHeader extends Header {

    private ResponseStatusLine statusLine;

    protected ResponseHeader(ResponseStatusLine statusLine, Attribute attributes) {
        super(attributes);
        this.statusLine = statusLine;
    }

    public static ResponseHeader of(List<String> statusLine, Attribute attributes) {
        return new ResponseHeader(ResponseStatusLine.from(statusLine), attributes);
    }

    public static ResponseHeader from(String headerText) {
        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        List<String> statusLine = HttpRequestUtils.parseStatusLine(splittedHeaderTexts[0]);

        return ResponseHeader.of(statusLine, Attribute.attributeFrom(headerText));
    }

    @Override
    protected String getStatusLine() {
        return statusLine.toString();
    }
}
