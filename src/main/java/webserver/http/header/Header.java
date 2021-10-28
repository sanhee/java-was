package webserver.http.header;

import webserver.http.attribute.Attributes;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class Header {
    private Attributes attributes;

    protected Header(Map<String, String> attributes) {
        this.attributes = Attributes.from(attributes);
    }

    protected Header(Attributes attributes) {
        this.attributes = attributes;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public byte[] getBytes() {
        StringBuilder sb = new StringBuilder();

        sb.append(getStatusLine()).append(System.lineSeparator());

        String attributesString = attributes.toString();

        sb.append(attributesString + (!attributesString.isEmpty() ? "\r\n" : ""));

        sb.append(System.lineSeparator());

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    protected abstract String getStatusLine();
}
