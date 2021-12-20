package webserver.http.header;

import util.HttpRequestUtils;
import webserver.Const;
import webserver.http.attribute.Attributes;

import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class Header {
    private Attributes attributes;

    protected Header(Attributes attributes) {
        this.attributes = attributes;
    }

    public static List<String> parseStartLine(String headerText) {
        String[] splittedHeaderTexts = headerText.split(Const.CRLF);
        return HttpRequestUtils.parseStartLine(splittedHeaderTexts[0]);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public byte[] getBytes() {
        StringBuilder sb = new StringBuilder();

        String attributesString = attributes.toHeaderText();
        sb.append(attributesString + (!attributesString.isEmpty() ? Const.CRLF : ""));
        sb.append(Const.CRLF);

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
