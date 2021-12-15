package webserver.http.startline;

import webserver.http.attribute.Attributes;

public abstract class StartLine {
    protected static final String PROTOCOL_VERSION_KEY = "protocolVersion";

    private Attributes attributes;

    public StartLine(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getProtocol() {
        return attributes.get(PROTOCOL_VERSION_KEY);
    }

    protected String getAttributeBy(String key) {
        return attributes.get(key);
    }
}
