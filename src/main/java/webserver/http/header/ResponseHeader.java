package webserver.http.header;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseHeader extends Header {
    protected static final String STATUS_CODE_KEY = "statusCode";
    protected static final String STATUS_TEXT_KEY = "statusText";

    protected ResponseHeader(Map<String, String> statusLineAttributes, Map<String, String> attributes) {
        super(statusLineAttributes, attributes);
    }

    public static ResponseHeader of(List<String> statusLine, Map<String, String> attributes) {
        Map<String, String> statusLineAttributes = new HashMap<>();

        statusLineAttributes.put(PROTOCOL_VERSION_KEY, statusLine.get(0));
        statusLineAttributes.put(STATUS_CODE_KEY, statusLine.get(1));
        statusLineAttributes.put(STATUS_TEXT_KEY, statusLine.get(2));

        return new ResponseHeader(statusLineAttributes, attributes);
    }

    @Override
    protected String statusLine() {
        return statusLineAttributes.get(PROTOCOL_VERSION_KEY) + " " + statusLineAttributes.get(STATUS_CODE_KEY) + " " + statusLineAttributes.get(STATUS_TEXT_KEY);
    }
}
