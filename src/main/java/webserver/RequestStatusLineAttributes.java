package webserver;

import java.util.Map;

public class RequestStatusLineAttributes {
    private Map<String, String> statusLineAttributes;

    public RequestStatusLineAttributes(Map<String, String> statusLineAttributes) {
        this.statusLineAttributes = statusLineAttributes;
    }

    public String method() {
        return statusLineAttributes.get(RequestHeader.METHOD_KEY);
    }

    public String path() {
        return statusLineAttributes.get(RequestHeader.PATH_KEY)
                .split("\\?")[0];

    }

    public String protocol() {
        return statusLineAttributes.get(RequestHeader.PROTOCOL_VERSION_KEY);
    }
}
