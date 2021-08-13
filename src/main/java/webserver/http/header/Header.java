package webserver.http.header;

import util.HttpRequestUtils;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Header {
    private Map<String, String> attributes;

    protected Header(Map<String, String> attributes) {
        this.attributes = attributes;
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

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public byte[] getBytes() {
        StringBuilder sb = new StringBuilder();

        sb.append(statusLine()).append(System.lineSeparator());

        for (Map.Entry<String, String> entry : getAttributes().entrySet()) {
            sb.append(entry.getKey() + ": " + entry.getValue() + System.lineSeparator());
        }

        sb.append(System.lineSeparator());

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    protected abstract String statusLine();
}
