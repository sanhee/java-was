package webserver.http.header;

import webserver.Const;
import webserver.http.attribute.Attributes;

import java.nio.charset.StandardCharsets;

public class ResponseHeader extends Header {

    protected ResponseHeader(Attributes attributes) {
        super(attributes);
    }

    public static ResponseHeader from(Attributes attributes) {
        return new ResponseHeader(attributes);
    }

    public static ResponseHeader from(String headerText) {
        return ResponseHeader.from(Attributes.from(headerText));
    }

    public byte[] getBytes() {
        StringBuilder sb = new StringBuilder();

        String attributesString = getAttributes().toHeaderText();

        sb.append(attributesString + (!attributesString.isEmpty() ? Const.CRLF : ""));

        sb.append(Const.CRLF);

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
