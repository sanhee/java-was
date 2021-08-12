package webserver.http.statusline;

import java.util.Map;

public class ResponseStatusLine extends StatusLine {
    private static final String STATUS_CODE_KEY = "statusCode";

    public ResponseStatusLine(Map<String, String> statusLineAttributes) {
        super(statusLineAttributes);
    }

    public String statusCode() {
        return statusLineAttributeBy(STATUS_CODE_KEY);
    }
}
