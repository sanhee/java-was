package webserver.http.header;

import webserver.http.attribute.Attributes;
import webserver.http.statusline.ResponseStatusLine;

import java.util.List;

public class ResponseHeader extends Header {

    private ResponseStatusLine statusLine;

    protected ResponseHeader(ResponseStatusLine statusLine, Attributes attributes) {
        super(attributes);
        this.statusLine = statusLine;
    }

    public static ResponseHeader of(List<String> statusLine, Attributes attributes) {
        return new ResponseHeader(ResponseStatusLine.from(statusLine), attributes);
    }

    public static ResponseHeader from(String headerText) {
        return ResponseHeader.of(parseStatusLine(headerText), Attributes.from(headerText));
    }

    @Override
    protected String getStatusLine() {
        return statusLine.toString();
    }
}
