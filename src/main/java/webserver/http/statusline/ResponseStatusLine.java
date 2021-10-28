package webserver.http.statusline;

import webserver.http.Attribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ResponseStatusLine extends StatusLine {
    private static final String STATUS_CODE_KEY = "statusCode";
    private static final String STATUS_TEXT_KEY = "statusText";

    public ResponseStatusLine(Attribute statusLineAttributes) {
        super(statusLineAttributes);
    }

    public static ResponseStatusLine from(List<String> statusLine) {
        Map<String, String> statusLineAttributes = new HashMap<>();

        statusLineAttributes.put(PROTOCOL_VERSION_KEY, statusLine.get(0));
        statusLineAttributes.put(STATUS_CODE_KEY, statusLine.get(1));
        statusLineAttributes.put(STATUS_TEXT_KEY, statusLine.get(2));

        return new ResponseStatusLine(Attribute.from(statusLineAttributes));
    }

    public String getStatusCode() {
        return getStatusLineAttributeBy(STATUS_CODE_KEY);
    }

    public String getStatusText() {
        return getStatusLineAttributeBy(STATUS_TEXT_KEY);
    }

    @Override
    public String toString() {
        return new StringJoiner(" ").add(getProtocol())
                                    .add(getStatusCode())
                                    .add(getStatusText())
                                    .toString();
    }
}
