package webserver;

import java.util.Map;

public class StatusLineAttributes {
    private Map<String, String> statusLineAttributes;

    public StatusLineAttributes(Map<String, String> statusLineAttributes) {
        this.statusLineAttributes = statusLineAttributes;
    }


    public String method() {
        return statusLineAttributes.get(RequestHeader.METHOD_KEY);
    }
}
