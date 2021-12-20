package webserver.http.header;

import webserver.Const;
import webserver.http.attribute.Attributes;
import webserver.http.startline.StatusLine;

import java.nio.charset.StandardCharsets;
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

    protected String getStartLine() {
        return statusLine.toString();
    }

    public byte[] getBytes() {
        StringBuilder sb = new StringBuilder();

        sb.append(getStartLine()).append(Const.CRLF);

        String attributesString = getAttributes().toHeaderText();

        sb.append(attributesString + (!attributesString.isEmpty() ? Const.CRLF : ""));

        sb.append(Const.CRLF);

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
