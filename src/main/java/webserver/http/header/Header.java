package webserver.http.header;

import webserver.http.Attribute;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class Header {
    private Attribute attributesNew;

    protected Header(Attribute attributes) {
        this.attributesNew = attributes;
    }

    public Attribute getAttributes() {
        return attributesNew;
    }

    public byte[] getBytes() {
        StringBuilder sb = new StringBuilder();

        sb.append(getStatusLine()).append(System.lineSeparator());

        for (Map.Entry<String, String> entry : getAttributes().entrySet()) {
            sb.append(entry.getKey() + ": " + entry.getValue() + System.lineSeparator());
        }

        sb.append(System.lineSeparator());

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    protected abstract String getStatusLine();
}
