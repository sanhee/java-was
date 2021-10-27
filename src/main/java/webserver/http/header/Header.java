package webserver.http.header;

import util.HttpRequestUtils;
import webserver.http.attribute.Attributes;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Header {
    private Attributes attributes;

    protected Header(Map<String, String> attributes) {
        this.attributes = Attributes.from(attributes);
    }

    protected static Map<String, String> attributeFrom(String headerText) {
        Map<String, String> attributes = new LinkedHashMap<>();

        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        for (String splittedHeaderText : splittedHeaderTexts) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(splittedHeaderText);

            if (pair != null) {
                attributes.put(pair.getKey(), pair.getValue());
            }
        }

        return attributes;
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
