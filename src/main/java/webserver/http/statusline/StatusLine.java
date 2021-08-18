package webserver.http.statusline;

import java.util.Map;

public abstract class StatusLine {
    protected static final String PROTOCOL_VERSION_KEY = "protocolVersion";

    private Map<String, String> statusLineAttributes;

    public StatusLine(Map<String, String> statusLineAttributes) {
        this.statusLineAttributes = statusLineAttributes;
    }

    public String getProtocol() {
        return statusLineAttributes.get(PROTOCOL_VERSION_KEY);
    }

    protected String getStatusLineAttributeBy(String key) {
        return statusLineAttributes.get(key);
    }
}
