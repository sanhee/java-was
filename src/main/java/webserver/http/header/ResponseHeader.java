package webserver.http.header;

import util.HttpRequestUtils;
import webserver.Const;
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
        String[] splittedHeaderTexts = headerText.split(Const.CRLF);
        List<String> statusLine = HttpRequestUtils.parseStatusLine(splittedHeaderTexts[0]);

        return ResponseHeader.of(statusLine, Attributes.from(headerText));
    }

    @Override
    protected String getStatusLine() {
        return statusLine.toString();
    }
}
