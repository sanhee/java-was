package webserver.http.header;

import webserver.http.attribute.Attributes;
import webserver.http.startline.StatusLine;

import java.util.List;

public class ResponseHeader extends Header {

    private StatusLine statusLine;

    protected ResponseHeader(StatusLine statusLine, Attributes attributes) {
        super(attributes);
        this.statusLine = statusLine;
    }

    public static ResponseHeader of(List<String> statusLine, Attributes attributes) {
        return new ResponseHeader(StatusLine.from(statusLine), attributes);
    }

    public static ResponseHeader from(String headerText) {
        return ResponseHeader.of(parseStartLine(headerText), Attributes.from(headerText));
    }

    @Override
    protected String getStatusLine() {
        return statusLine.toString();
    }
}
