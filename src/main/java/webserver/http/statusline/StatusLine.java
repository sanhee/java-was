package webserver.http.statusline;

import webserver.http.Attribute;

public abstract class StatusLine {
    protected static final String PROTOCOL_VERSION_KEY = "protocolVersion";

    private Attribute statusLineAttributes;

    public StatusLine(Attribute statusLineAttributes) {
        this.statusLineAttributes = statusLineAttributes;
    }

    public String getProtocol() {
        return statusLineAttributes.get(PROTOCOL_VERSION_KEY);
    }

    protected String getStatusLineAttributeBy(String key) {
        return statusLineAttributes.get(key);
    }
}
