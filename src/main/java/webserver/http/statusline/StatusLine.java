package webserver.http.statusline;

import java.util.Map;

public abstract class StatusLine {
    protected static final String PROTOCOL_VERSION_KEY = "protocolVersion";

    private Map<String, String> statusLineAttributes;

    public StatusLine(Map<String, String> statusLineAttributes) {
        this.statusLineAttributes = statusLineAttributes;
    }

    public String protocol() {
        return statusLineAttributes.get(PROTOCOL_VERSION_KEY);
    }

    protected String statusLineAttributeBy(String key) {
        return statusLineAttributes.get(key);
    }
}
