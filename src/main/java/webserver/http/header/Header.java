package webserver.http.header;

import util.HttpRequestUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class Header {
    public static final String PROTOCOL_VERSION_KEY = "protocolVersion";

    protected Map<String, String> statusLineAttributes;
    private Map<String, String> attributes;

    public Header(Map<String, String> attributes) {
        this.attributes = attributes;
        this.statusLineAttributes = new HashMap<>();
    }

    public Header(Map<String, String> statusLineAttributes, Map<String, String> attributes) {
        this.statusLineAttributes = statusLineAttributes;
        this.attributes = attributes;
    }

    public static RequestHeader requestHeaderFrom(String headerText) {
        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        List<String> statusLine = HttpRequestUtils.parseStatusLine(splittedHeaderTexts[0]);

        return RequestHeader.of(statusLine, attributeFrom(headerText));
    }

    public static ResponseHeader responseHeaderFrom(String headerText) {
        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        List<String> statusLine = HttpRequestUtils.parseStatusLine(splittedHeaderTexts[0]);

        return ResponseHeader.of(statusLine, attributeFrom(headerText));
    }

    private static Map<String, String> attributeFrom(String headerText) {
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

    public Map<String, String> getStatusLineAttributes() {
        return statusLineAttributes;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return Objects.equals(statusLineAttributes, header.statusLineAttributes) && Objects.equals(attributes, header.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusLineAttributes, attributes);
    }

    @Override
    public String toString() {
        return "Header{" +
                "statusLineAttributes=" + statusLineAttributes +
                ", attributes=" + attributes +
                '}';
    }
}
