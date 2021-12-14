package webserver.http.startline;

import webserver.http.attribute.Attributes;

import java.util.Map;

public abstract class StartLine {
    protected static final String PROTOCOL_VERSION_KEY = "protocolVersion";

    private Attributes attributes;

    public StartLine(Map<String, String> attributes) {
        this.attributes = Attributes.from(attributes);
    }

    public String getProtocol() {
        return attributes.get(PROTOCOL_VERSION_KEY);
    }

    protected String getAttributeBy(String key) {
        return attributes.get(key);
    }
}
